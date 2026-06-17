# Probability Density Analysis (Monte Carlo)

# Descripción general

El algoritmo **Probability Density Analysis** (Análisis de Densidad de Probabilidad, comúnmente llamado estrategia de **Monte Carlo**) es una de las estrategias de resolución de Battleship más eficientes en términos de cantidad de disparos. A diferencia de los métodos heurísticos rígidos o puramente aleatorios, esta estrategia utiliza un enfoque probabilístico continuo para determinar el siguiente disparo óptimo.

En cada turno, el resolvedor simula todas las formas posibles en las que los barcos restantes de la flota enemiga podrían estar posicionados en el tablero, respetando la información pública disponible (disparos fallidos e impactos acumulados). Con base en estas simulaciones, genera un **Mapa de Calor** (Heatmap) o matriz de densidad de probabilidad, y dispara a la coordenada libre que registre la mayor probabilidad de contener un barco.

---

# Funcionamiento detallado

El algoritmo calcula la probabilidad de cada casilla del tablero mediante los siguientes pasos:

### 1. Registro de la Flota Restante
* Se mantiene un control estricto de los barcos enemigos que aún siguen a flote (por ejemplo: Carrier de 5 celdas, Battleship de 4, etc.).
* Al hundir un barco, este se elimina del conjunto de barcos a simular, lo que afina significativamente la precisión del mapa de calor en los turnos posteriores.

### 2. Generación del Mapa de Calor (Heatmap)
Para cada casilla del tablero (del índice `0` al `99` en un tablero estándar de $10 \times 10$), se inicializa un contador de frecuencia en `0`.
Para cada barco restante en la flota enemiga:
* Se evalúan todas las posiciones y orientaciones posibles (horizontal y vertical) dentro del tablero.
* Para cada posición tentativa, se verifica su validez:
  1. **Límites:** El barco debe caber completamente dentro del tablero sin desbordarlo.
  2. **Consistencia con el historial:** Ninguna celda ocupada por el barco en esa configuración puede coincidir con un disparo previo que haya resultado en agua (`CellState.MISS`).
  3. **Consistencia con impactos activos:** Si existen celdas impactadas (`CellState.HIT`) que aún no pertenecen a barcos completamente hundidos, las configuraciones del barco que no cubran al menos una de estas celdas impactadas son descartadas o filtradas.
* Si la posición tentativa es válida, se incrementa en `1` el contador de frecuencia de todas las casillas que el barco ocuparía bajo esa configuración.

Al finalizar el análisis de todos los barcos de la flota, se obtiene una matriz de frecuencias (densidades).

### 3. Selección del Disparo
* El resolvedor filtra las casillas del mapa de calor para considerar únicamente aquellas a las que no se les ha disparado.
* Se selecciona la coordenada que tenga la frecuencia más alta en el mapa de calor.
* En caso de empate, se puede desempatar de forma aleatoria o priorizando las casillas más cercanas al centro del tablero.

---

# Modos de Operación

El resolvedor alterna implícita y dinámicamente entre dos modos de operación basándose en el estado del tablero:

### A. Modo de Búsqueda (Hunt Mode)
Cuando **no hay impactos activos** en el tablero (todas las celdas con barcos detectados ya pertenecen a barcos completamente hundidos, o no se ha descubierto ningún barco todavía):
* Las configuraciones válidas se distribuyen de manera uniforme sobre todo el espacio disponible.
* Las casillas centrales acumulan de forma natural una densidad mucho mayor de configuraciones válidas, ya que los barcos largos disponen de más espacio para acomodarse en el centro sin salirse de los límites del tablero. Esto guía al algoritmo a realizar disparos iniciales concentrados en el centro, lo cual es estadísticamente óptimo.

### B. Modo de Caza (Target Mode)
Cuando **hay al menos un impacto (`HIT`) activo** (un barco ha sido tocado pero no se ha hundido por completo):
* El algoritmo restringe las simulaciones para que las configuraciones de los barcos restantes **crucen obligatoriamente** las coordenadas de los impactos activos.
* Esto genera un pico masivo de probabilidad en las casillas adyacentes a los impactos, forzando al resolvedor a disparar alrededor de los aciertos y a seguir la línea de orientación del barco hasta hundirlo.

---

# Optimización e Implementación de Alto Rendimiento

El Análisis de Densidad de Probabilidad es computacionalmente exigente en comparación con estrategias simples como *Hunt and Target* o *Brute Force*, requiriendo miles de validaciones de tableros por cada turno. Para integrarlo de forma viable en las simulaciones masivas (500 000 partidas) de este repositorio sin perjudicar drásticamente los tiempos en los benchmarks de JMH, se implementan las siguientes optimizaciones críticas:

### 1. Precálculo del Mapa Base (Base Heatmap Cache)
* **El problema:** En el primer turno, el tablero está completamente vacío. Evaluar todas las combinaciones posibles para toda la flota en un tablero limpio de $10 \times 10$ requiere la misma cantidad de ciclos computacionales exactos en cada una de las 500 000 ejecuciones de la simulación.
* **La solución:** El mapa de calor para un tablero en estado inicial (vacío) se calcula **una única vez** (durante la inicialización de la clase o de forma estática) y se almacena en memoria como una plantilla de sólo lectura.
* **Copia rápida en `reset()`:** Cuando se inicia una nueva partida y se invoca el método `reset()` de la estrategia, se copia el mapa de calor precalculado usando una operación de bajo nivel muy rápida (`System.arraycopy`) en lugar de rehacer todo el cálculo.

### 2. Estructuras planas y Zero-Allocation
* El mapa de calor y el estado de las celdas se gestionan como arreglos planos de una dimensión (`int[]` y `byte[]`), evitando la sobrecarga de asignación de memoria (GC pressure) asociada con arreglos bidimensionales o colecciones de objetos `Coordinate`.
* Las operaciones y recorridos se realizan utilizando aritmética de coordenadas lineales (por ejemplo, sumando `1` para avanzar en horizontal y `BOARD_DIMENSION` para avanzar en vertical).

### 3. Poda Temprana de Bucles (Early Pruning)
* Al evaluar la validez de una colocación de barco, si la validación encuentra una celda con agua/miss o fuera de límites, el bucle se aborta inmediatamente (`break`) para no procesar las celdas restantes del barco.
