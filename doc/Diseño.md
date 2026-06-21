# Desiciones de diseño

# Arquitectura
Se siguen los principios fundamentales de **Clean Architecture**, pero aplicada de una forma pragmática y optimizada para el rendimiento (evitando la sobreingeniería típica de DTOs y mapeadores excesivos). Se mantiene una separación de responsabilidades clara y desacoplada a través de interfaces, organizada en los siguientes paquetes principales:
- config: Centraliza las constantes de configuración y parámetros globales del juego (ej. dimensiones del tablero).
- domain: Define las reglas del negocio y el modelo de dominio (tableros, vistas protegidas como BoardView y el contenido de celdas).
- engine: Controla el motor de juego, la simulación de partidas y la recolección masiva de métricas y estadísticas.
- strategy: Encapsula la interfaz central BattleshipStrategy y los diversos solucionadores (solvers) bajo el patrón Strategy.
- benchmarks / benchmark: Módulo de pruebas de microrendimiento (JMH) para comparar el costo computacional de cada combinación de tablero y estrategia.
- gui: Capa visual e interactiva totalmente independiente del núcleo del simulador.

### ¿Por qué Clean Architecture y no un Motor de Simulación Tradicional?
1. **Aislamiento de Estrategias (Open-Closed):** El objetivo principal del proyecto es comparar solvers independientes. El desacoplamiento mediante el patrón Strategy permite agregar nuevas tácticas de resolución (como Monte Carlo o Paridad) de forma 100% aislada, sin alterar el motor del juego ni las reglas de dominio.
2. **Seguridad y Regla de Dependencia (`BoardView`):** En un motor de simulación puro, todo el estado suele estar abierto para mayor velocidad. En cambio, Clean Architecture nos permite establecer una frontera de protección rígida (`BoardView`) para que los solucionadores solo accedan a información pública (agua, impacto, etc) y no puedan "hacer trampa" leyendo las coordenadas secretas de los barcos en memoria.
3. **Desacoplamiento de la Interfaz (UI y Benchmarks):** Las reglas de negocio son independientes de los mecanismos de entrega. Esto facilita ejecutar pruebas de rendimiento masivas por consola (JMH) o levantar una interfaz gráfica interactiva (GUI) usando exactamente el mismo núcleo, sin dependencias circulares.
4. **Diseño Híbrido (Macro vs. Micro):** Se adopta Clean Architecture a nivel macro para organizar dependencias y asegurar la mantenibilidad a largo plazo, pero se aplican técnicas de motores de simulación (DOD / Programación Orientada a Datos, aplanado de arreglos y reutilización de memoria) a nivel micro dentro del dominio para maximizar la velocidad.


# Reglas de Juego (Hasbro)

El simulador se rige por las reglas oficiales del juego de mesa **Battleship de Hasbro**, las cuales definen las mecánicas y determinan el diseño del dominio:

### 1. Dimensiones del Tablero
El área de juego consta de una cuadrícula bidimensional de **10x10 celdas** (un total de 100 coordenadas de disparo).

### 2. Composición de la Flota (17 Celdas de Salud)
Cada jugador cuenta con una flota estándar de **5 barcos**, cuyas longitudes y correspondencias en `CellContent` son:
* **Portaaviones (Carrier):** 5 celdas.
* **Acorazado (Battleship):** 4 celdas.
* **Crucero (Cruiser):** 3 celdas.
* **Submarino (Submarine):** 3 celdas.
* **Destructor (Destroyer):** 2 celdas.

### 3. Mecánica de Revelación de Impacto (Hasbro Rule)
* Al realizar un disparo, el defensor debe responder si ha sido **Agua (Miss)** o **Impacto (Hit)**.
* **Revelación de Identidad:** De acuerdo con la regla oficial de Hasbro, al acertar un disparo, el defensor debe anunciar **el tipo específico de barco que ha sido impactado** (ej. *"¡Tocado! Le diste a mi Acorazado"*). Del mismo modo, debe anunciar cuándo un barco ha sido completamente hundido (*"¡Hundiste mi Acorazado!"*).
* **Decisión de Diseño Asociada:** Esta regla es el motivo principal por el cual la cuadrícula interna del tablero almacena los IDs específicos de cada barco (`1` al `5` de `CellContent`) en lugar de un estado genérico de "barco". Esto permite que la `BoardView` devuelva el tipo de barco impactado a la estrategia en tiempo de ejecución, permitiendo a los solucionadores (solvers) optimizar sus búsquedas basándose en qué barco específico están cazando.

### 4. Condición de Fin de Partida
La partida concluye inmediatamente cuando se logra hundir la flota completa del oponente, lo cual equivale a registrar exactamente **17 impactos exitosos**.


 # Rendimiento


 El rendimiento es el enfoque primordial de la aplicación y la justificación detrás de cada decisión estructural. Debido a que el simulador ejecuta millones de operaciones por segundo, se han aplicado técnicas avanzadas de optimización de bajo nivel y diseño orientado a datos:

 ### 1. Dualidad de Paradigmas: Programación Orientada a Datos (POD)
 A nivel de micro-dominio (tablero y celdas), se evitan los Enums y la instanciación de objetos. 
 * **Localidad Espacial y Caché:** Al usar arreglos planos de tipos primitivos (`byte[]` y `boolean[]`), los datos del tablero se almacenan de forma contigua en la memoria física. Esto permite que la CPU cargue el tablero completo en menos de dos líneas de caché (*Cache Lines* de 64 bytes), reduciendo los *Cache Misses* a cero.
 * **Aritmética de Índices:** Las transiciones de celdas vecinas se resuelven mediante operaciones aritméticas directas en registros de CPU (ej. `coord + 1` para la derecha), evitando la desreferenciación de punteros en el Heap.

 ### 2. Hot Paths Libres de Alocación (Zero-Allocation)
 La asignación y liberación constante de objetos en bucles críticos genera una alta presión sobre el recolector de basura (*GC Pressure*), provocando pausas de ejecución (*Stop-the-World*).
 * **Reutilización de Memoria (`reset`):** Se prioriza la reutilización de objetos pre-asignados. El tablero y las estrategias se limpian mediante métodos `reset()` y funciones nativas altamente optimizadas como `Arrays.fill` o `System.arraycopy` (que compilan a instrucciones de copia de memoria por hardware).
 * **Patrón Out-Parameter (Mutación In-Place):** Se evita que los métodos instancien y retornen nuevos arreglos en caliente. En su lugar, el invocador provee un arreglo pre-asignado como argumento (ej. `CellContent.copyLengths(byte[] destination)`). El método muta los datos directamente sobre este contenedor preexistente, logrando una operación libre de alocaciones y maximizando la velocidad al aprovechar la localidad de caché.
 * **Impacto:** En una simulación estándar de 500 000 partidas, se logra reducir la creación de objetos de varias decenas de millones a una cantidad inferior a 1,000 objetos totales en todo el ciclo de vida del programa.

 ### 3. Flyweight de Primitivos (`CellContent`)
 Se desacopla el estado de la celda de las propiedades de los barcos. La clase final `CellContent` centraliza las longitudes inmutables de los barcos en un único arreglo estático e inmutable. 
 * **Inlining y ABCE:** Al encapsular el arreglo tras el método estático `getLength(byte)`, el compilador JIT (Just-In-Time) realiza *inlining* completo del método y aplica la optimización *ABCE (Array Bounds Check Elimination)*, eliminando las ramas condicionales redundantes de límites a nivel de código máquina.

 ### 4. Concurrencia de Alto Rendimiento y Libre de Bloqueos
 Para garantizar la escalabilidad lineal en procesadores multinúcleo dentro de `MetricAnalyzer`:
 * **Aislamiento de Hilos (Thread Confinement):** Cada hilo de ejecución trabaja sobre sus propias instancias locales de tableros y estrategias, evitando cualquier necesidad de sincronización o bloqueos de concurrencia.
 * **Acumuladores sin Contención:** Se evita el uso de variables volátiles o primitivos sincronizados para métricas compartidas. En su lugar, se utilizan clases utilitarias del paquete de concurrencia de Java como `LongAdder` y `LongAccumulator`, que minimizan la invalidación de caché entre núcleos de la CPU.

 ### Metodología de Benchmarking
 Para validar científicamente el rendimiento, se utilizan 2 tipos de mediciones sobre muestras masivas de 500 000 partidas:
 1. **Benchmarks Basados en Tiempo (JMH):** Pruebas de microrendimiento que miden el tiempo promedio de ejecución por operación (`ms/op`) aislando el comportamiento de la JVM (calentamiento, optimizaciones JIT y GC).
 2. **Benchmarks Basados en Eficiencia (Métricas del Juego):** Evalúan la calidad matemática de los solucionadores (turnos promedio para resolver el tablero, juegos perfectos, mejor y peor caso).


 # Manejo de validaciones


Por decisiones de rendimiento y diseño de arquitectura, se ha optado por omitir las validaciones manuales redundantes en las capas internas del dominio y el motor de simulación.
### 1. Diseño por Contrato (Design by Contract)
Los métodos del dominio (ej. posicionar barcos, procesar disparos) definen precondiciones estrictas sobre sus argumentos (como coordenadas en rango `0..99` o identificadores de barcos válidos).
* **Violación de Contrato:** Si un componente interno envía un dato fuera de rango, se considera un error de programación (bug). 
* **Filosofía Fail-Fast:** El sistema delega la validación de límites directamente a la máquina virtual (JVM) a través de excepciones nativas (como `ArrayIndexOutOfBoundsException`). Esto permite detectar fallos al instante durante la fase de desarrollo y tests unitarios, en lugar de intentar mitigar o tolerar estados inconsistentes.
### 2. Optimización de Bifurcaciones (Branching Minimization)
En los bucles calientes (*hot paths*) de las simulaciones masivas (500 000 partidas), cada validación `if` introduce instrucciones de control en la CPU. Evitar estas validaciones manuales previene penalizaciones por fallos en la predicción de ramas (*branch misprediction*) y permite al compilador JIT aplicar optimizaciones avanzadas (como *Loop Unrolling* y *Array Bounds Check Elimination*).
### 3. Validación en la Frontera (Boundary Sanitization)
Esta política de omisión de validaciones solo aplica a la comunicación interna entre el dominio, el motor y las estrategias. 
* **Frontera Segura (GUI / Input):** Toda entrada externa de datos (como las coordenadas ingresadas por un jugador a través de la GUI) debe ser exhaustivamente validada e interceptada en la capa periférica antes de interactuar con el modelo de dominio. El dominio solo recibe datos previamente saneados por los controladores de la interfaz.