# Hunt and Target

# Descripción general

El algoritmo **Hunt and Target** (Cazar y Apuntar) es la estrategia intuitiva que la mayoría de los seres humanos utiliza de forma natural al jugar Battleship.

Consiste en alternar dinámicamente entre dos modos de operación según los resultados de los disparos:
1. **Modo Hunt (Caza):** Se realizan disparos aleatorios sobre el tablero para localizar la ubicación de algún barco.
2. **Modo Target (Objetivo):** Una vez que se logra un impacto (*Hit*), el algoritmo entra en modo de ataque concentrado, disparando a las celdas adyacentes al impacto para hundir el barco antes de regresar al modo de caza.

---

# Funcionamiento detallado

### 1. Modo Hunt (Caza)
* El resolvedor selecciona celdas al azar de la lista de coordenadas que aún no han recibido disparos.
* Se mantiene en este modo hasta que el resultado de un disparo sea `CellState.HIT`.
* Cuando detecta un impacto, transiciona inmediatamente al **Modo Target**.

### 2. Modo Target (Objetivo)
* Al producirse un impacto en una coordenada, se identifican sus 4 celdas vecinas adyacentes (Arriba, Abajo, Izquierda y Derecha) que estén dentro de los límites del tablero.
* Si estas celdas vecinas no han sido disparadas previamente (es decir, su estado visible es `CellState.WATER`), se añaden a una pila (*LIFO stack*) de celdas objetivo (`targets`).
* En los siguientes turnos, el resolvedor extrae la coordenada del tope de la pila de objetivos y dispara a ella.
* Si este disparo en modo Target resulta en otro impacto (`HIT`), se calculan y añaden sus celdas vecinas a la pila de objetivos, lo que permite seguir la orientación del barco de forma natural.
* Si no quedan celdas válidas en la pila de objetivos (se han agotado o ya fueron disparadas), el algoritmo asume que el barco ha sido hundido y regresa al **Modo Hunt**.

---

# Optimización e Implementación de Alto Rendimiento

Para maximizar el rendimiento del simulador multihilo y minimizar las asignaciones de memoria en la JVM, se implementaron las siguientes optimizaciones de bajo nivel en `HuntTargetStrategy.java`:

### Búsqueda Aleatoria en O(1) (Fisher-Yates)
Para el modo Hunt, se mantiene un arreglo `emptyCells` con las coordenadas disponibles y un contador `remainingCells`. Al seleccionar un índice al azar:
1. Se obtiene la celda en ese índice.
2. Se reemplaza el valor de esa posición por el valor ubicado al final del arreglo disponible (`remainingCells - 1`).
3. Se reduce `remainingCells` en 1.
Esto evita tener que hacer búsquedas aleatorias repetitivas o filtrar listas dinámicas.

### Eliminación en O(1) de Celdas Objetivas (`cellToIndex`)
Cuando el resolvedor dispara en modo **Target**, debe remover esa coordenada de la lista de celdas disponibles para el modo Hunt (`emptyCells`) para evitar dispararle en el futuro. 
* Una búsqueda secuencial tradicional costaría $O(N)$ (donde $N$ es la cantidad de celdas restantes).
* **Solución optimizada:** Se introdujo un arreglo de mapeo inverso llamado `cellToIndex`. Este arreglo almacena la posición exacta de cada coordenada en el arreglo `emptyCells`.
* Al realizar la eliminación:
  1. Se consulta el índice en `cellToIndex[coord]` en tiempo constante $O(1)$.
  2. Se realiza el intercambio del elemento a eliminar con el último elemento de `emptyCells` en $O(1)$.
  3. Se actualizan las referencias correspondientes en `cellToIndex`.
  
Esta optimización de $O(1)$ redujo a la mitad el tiempo de ejecución en los benchmarks de JMH (de **~176 ms/op** a **~92 ms/op** para 500 000 partidas en `Board1D`).
