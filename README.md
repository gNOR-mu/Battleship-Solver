# Descripción general

Este es un repositorio inspirado en un proyecto que tuve que hacer en algún momento de la universidad en la asignatura de Análisis y Diseños de Algoritmos (ADA), sobre resolver un Battleship en la menor cantidad de movimientos posibles.

La idea del repositorio es simple, mostrar diversos algoritmos y sus funcionamientos como estrategias para resolver el tablero de forma eficiente e ineficiente según el algoritmo utilizado mediante el uso del patrón de diseño Strategy.

Además se toman optimizaciones adicionales, como caching, aplanado de arreglos, etc. Para reducir en mayor medida el tiempo de resolución.

# Uso con Maven

Compilación: 
```shell 
mvn compile
```

Ejecución: 
```shell 
mvn exec:exec
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

# TODO

### General:
- [ ] Documentar cada solver con una descripción detallada de su funcionamiento.
- [ ] Definir framework para la GUI.


### Implementar algoritmos:

Descripción breve de los algoritmos a implementar:

- [X] [Brute Force](doc/Brute%20force.md): Recorre el tablero como si estuviera leyendo un libro, de izquierda a derecha y de arriba abajo.
- [X] [True Random](doc/True%20random.md): Algoritmo más ineficiente, dispara aleatoriamente en una coordenada al azar.
- [ ] [Random Search](doc/Random%20search.md): algoritmo básico. Simplemente, elige una coordenada al azar que no haya sido disparada antes ignorando si impactó.
- [ ] [Hunt and Target](doc/Hunt%20and%20target.md): Es el algoritmo que la mayoría de los humanos usamos de forma intuitiva.
  - Modo Hunt: Dispara al azar hasta que encuentra un barco (un "Hit"). 
  - Modo Target: Una vez que acierta, deja de disparar al azar y empieza a probar las celdas adyacentes (arriba, abajo, izquierda, derecha) hasta hundir el barco.
- [ ] [Parity / Checkerboard](doc/Parity%20checkerboard.md) (Paridad o Tablero de Ajedrez): Optimiza la búsqueda basándose en una regla matemática
  - Ejemplo: el barco más pequeño ocupa 2 celdas.
  - Cómo funciona: Solo dispara en celdas de un color del tablero de ajedrez (donde x + y es par, por ejemplo). Es imposible que un barco de tamaño 2 o más se esconda sin tocar al menos una celda de ese color
- [ ] [Probability Density Analysis (Monte Carlo)](doc/Montecarlo.md): Algoritmo probabilístico, las celdas donde los barcos caben con más frecuencia son las que tienen mayor probabilidad de éxito.
- [ ] [Heuristic-Based](doc/Heuristic-based.md): Una combinación de reglas predefinidas.
