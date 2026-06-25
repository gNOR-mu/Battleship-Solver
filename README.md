# Descripción general

Este es un repositorio inspirado en un proyecto que tuve que hacer en algún momento de la universidad en la asignatura de Análisis y Diseños de Algoritmos (ADA), sobre resolver un Battleship en la menor cantidad de movimientos posibles.

La idea del repositorio es simple, mostrar diversos algoritmos y sus funcionamientos como estrategias para resolver el tablero de forma eficiente e ineficiente según el algoritmo utilizado mediante el uso del patrón de diseño Strategy.

Como objetivo adicional se intenta que la resolución de los tableros (500k simulaciones) sea lo más eficiente posible, aunque implique utilizar estrategias más abstractas que se alejen de los principios de POO.

Además se toman optimizaciones adicionales, como caching, aplanado de arreglos, etc. Para reducir en mayor medida el tiempo de resolución.

**Se va a considerar una victoria absoluta cuando un algoritmo sea capaz de resolver los 500k de juegos en menos de 1 milisegundo.**

# Uso con Maven

Ejecución: 
```shell 
mvn compile exec:exec
```

Ejecutar Benchmarks:
```shell
# Obligatorio compilar
mvn clean package "-Doutput.dir=target/maven-real-classes"

# Ejecutar TODOS los benchmarks
java -jar target/benchmarks.jar

```

Test: 
```shell 
mvn test
```

# Estructura de archivos

```
├───doc                                 # Documentación del proyecto.
├───src
│   ├───main
│   │   └───java
│   │       └───com
│   │           └───gnormu
│   │               └───battleship
│   │                   ├── App.java    # Archivo principal de Java.
│   │                   ├───domain      # Clases fundamentales que representan los datos del juego.
│   │                   ├───engine      # Controla el flujo de la aplicación.
│   │                   ├───gui         # Paquete separado exclusivamente para el entorno visual.
│   │                   └───strategy    # Incluye la interfaz central BattleshipStrategy y todas sus implementaciones concretas como RandomSearch, HuntAndTarget, etc.
│   └───test                            # Tests de archivos.
│       └...
```

# Consideraciones

- El código está en inglés pero la documentación en español
- Se utiliza ThreadLocalRandom para calcular la aleatoriedad el cual no es verdaderamente random, sin embargo para efectos de rendimiento se utiliza en vez de otras alternativas como SecureRandom.
- Las pruebas se ejecutan con 500 000 tableros.
- Debido a que la resolución de un tablero exige que se utilicen cientos de miles de veces los arreglos de barcos, utilizar una copia de los objetos afecta el rendimiento, por ello se ha optado implementar el patrón Flyweight para los barcos. Aunque se prevee que las resoluciones 500 000 tableros ronden el tiempo de 1-2 segundos, se pretende buscar otras formas que ayuden a estudiar como optimizar el rendimiento.
  - Opcionalmente se podría implementar una interfaz para el tablero y así ejecutar benchmarks de la forma tradicional y optimizada con el patrón Flyweight.

- Desiciones sobre manejo de coordenadas en [Manejo de coordenadas](doc/Manejo%20coordenadas.md)

- Reglas de diseño en [Archivo marksown](doc/Diseño.md)


### General:
- [ ] Documentar cada solver con una descripción detallada de su funcionamiento.
- [ ] Definir framework para la GUI.


### Implementar algoritmos:

Descripción breve de los algoritmos a implementar:

- [X] [Brute Force](doc/Brute%20force.md): Recorre el tablero como si estuviera leyendo un libro, de izquierda a derecha y de arriba abajo.
- [X] [True Random](doc/True%20random.md): Algoritmo más ineficiente, dispara aleatoriamente en una coordenada al azar.
- [X] [Hunt and Target](doc/Hunt%20and%20target.md): Es el algoritmo que la mayoría de los humanos usamos de forma intuitiva disparando al azar hasta encontrar algo y luego atacar sistemáticamente.
- [ ] [Parity / Checkerboard](doc/Parity%20checkerboard.md) (Paridad o Tablero de Ajedrez): Optimiza la búsqueda basándose en una regla matemática
  - Ejemplo: el barco más pequeño ocupa 2 celdas.
  - Cómo funciona: Solo dispara en celdas de un color del tablero de ajedrez (donde x + y es par, por ejemplo). Es imposible que un barco de tamaño 2 o más se esconda sin tocar al menos una celda de ese color
- [ ] [Probability Density Analysis (Monte Carlo)](doc/Montecarlo.md): Algoritmo probabilístico, las celdas donde los barcos caben con más frecuencia son las que tienen mayor probabilidad de éxito.
- [ ] [Heuristic-Based](doc/Heuristic-based.md): Una combinación de reglas predefinidas.

# Resultados

Evaluación en un Notebook i5-13420H 16GB RAM DDR5, ambas pruebas se ejecutan con 500 000 tableros, las cuales representan la operación, es decir 100 ms/op corresponde a una ejecución que engloba la resolución de 500 000 tableros tardando solo 100 ms en total, los tiempos son extremadamente sensibles según el uso de recursos actuales al momento de su ejecución.

### Tiempo Promedio en milisegundos (ms):
```
Benchmark                      (boardType)    (strategyType)  Mode  Cnt    Score    Error  Units
SolverBenchmark.runSimulation      Board1D        TrueRandom  avgt    5  118,669 ± 15,152  ms/op
SolverBenchmark.runSimulation      Board1D  TrueRandomMemory  avgt    5   50,615 ±  6,085  ms/op
SolverBenchmark.runSimulation      Board1D        BruteForce  avgt    5   23,584 ±  2,115  ms/op
SolverBenchmark.runSimulation      Board1D        HuntTarget  avgt    5  118,403 ± 10,443  ms/op
```

### Evaluación de turnos:
```
================================================================================
                    BATTLESHIP MULTISOLVER EVALUATOR
================================================================================
Partidas por combinación: 500.000
Dimensión del Tablero: 10x10
================================================================================

-------------------------------------------------------------------------------------------------------------------
| Tablero      | Estrategia                       | Turnos Prom. | Juegos Perfectos | Mejor Juego  | Peor Juego   |
-------------------------------------------------------------------------------------------------------------------
| Board 1D     | BruteForce                       | 88,52        | 0                | 26           | 100          |
| Board 1D     | TrueRandom - Sin memoria         | 343,73       | 0                | 64           | 1542         |
| Board 1D     | TrueRandom - Con memoria         | 95,40        | 0                | 53           | 100          |
| Board 1D     | HuntTarget                       | 59,77        | 0                | 18           | 100          |
-------------------------------------------------------------------------------------------------------------------
===================================================================================================================
```

### Otros

Existe una implementación FixedRandomPlacer la cual genera una única variante aleatoria de la posición de los barcos y luego los copia para cada juego, esto simula un posicionador de barcos casi perfecto en términos de tiempo o al menos más eficiente que el aleatorio indicando que aún hay márgenes de mejora, por sejemplo en BruteForce con casi 16 ms de diferencia:

```
SolverBenchmark.runSimulation      Board1D        Random        BruteForce  avgt    5   26,997 ± 2,948  ms/op
SolverBenchmark.runSimulation      Board1D   FixedRandom        BruteForce  avgt    5   10,709 ± 1,015  ms/op
```