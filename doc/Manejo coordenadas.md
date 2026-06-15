# Documentación: Sistema de Coordenadas Lineales Primitivas

Para maximizar el rendimiento y la eficiencia del simulador de Battleship, se ha eliminado la clase `Coordinate` y se ha implementado un sistema de **coordenadas lineales basadas en enteros primitivos (`int`)**.

---

## 1. ¿Por qué utilizar Coordenadas Lineales?

El uso de un entero primitivo plano aporta ventajas críticas para simulaciones de alta velocidad (JMH):
*   **Cero Asignación de Heap (Zero-Allocation):** Elimina la creación de millones de instancias de objetos de coordenadas en memoria durante las simulaciones recurrentes. Esto reduce a cero la presión sobre el Recolector de Basura (Garbage Collector).
*   **Localidad de Caché CPU (L1/L2):** Un tablero de juego se almacena como un arreglo plano `byte[]`. Acceder a una posición mediante una coordenada lineal es un acceso directo indexado en memoria contigua (`grid[coordinate]`), evitando indirecciones de punteros de objetos (Pointer Chasing).
*   **Optimización del Predictor de Saltos:** Al no requerir un caché de objetos con comprobaciones condicionales `if-else` en tiempo de ejecución, el procesador ejecuta instrucciones de forma lineal y ultra rápida sin riesgo de fallas de predicción de saltos (*branch misprediction*).

---

## 2. Representación y Límites de Rango

Dado un tablero cuadrado con una dimensión `D = GameConfig.BOARD_DIMENSION` (típicamente 10):
*   La **Coordenada Lineal** es un único entero en el rango `[0, (D * D) - 1]` (para dimensión 10, de `0` a `99`).
*   La celda superior izquierda `(0, 0)` se representa como `0`.
*   La celda inferior derecha `(9, 9)` se representa como `99`.

---

## 3. Fórmulas de Conversión y Recuperación

### De Bidimensional (Fila, Columna) a Lineal:
$$\text{coordenada} = (\text{fila} \times D) + \text{columna}$$

```java
int coordinate = (row * GameConfig.BOARD_DIMENSION) + column;
```

### De Lineal a Fila y Columna:
$$\text{fila} = \lfloor \text{coordenada} / D \rfloor$$
$$\text{columna} = \text{coordenada} \pmod D$$

```java
int row = coordinate / GameConfig.BOARD_DIMENSION;
int col = coordinate % GameConfig.BOARD_DIMENSION;
```

---

## 4. Recorridos y Desplazamientos en el Tablero

Trabajar de forma lineal simplifica la aritmética para desplazarse por el tablero en horizontal y vertical sin necesidad de reconstruir objetos.

### Recorrido Horizontal (Derecha / Izquierda)
Para avanzar o retroceder horizontalmente, sumamos o restamos `1` directamente a la coordenada lineal.

*   **Desplazamiento a la derecha:** `coordenada + 1`
*   **Desplazamiento a la izquierda:** `coordenada - 1`

*Ejemplo en posicionamiento de barcos horizontal:*
```java
// Posicionar un barco horizontalmente desde 'col' inicial:
for (int i = 0; i < length; i++) {
    int coord = row * GameConfig.BOARD_DIMENSION + (col + i);
    board.setCellState(coord, CellState.SHIP);
}
```

### Recorrido Vertical (Abajo / Arriba)
Para movernos en sentido vertical, sumamos o restamos el tamaño de la dimensión del tablero `D` (`GameConfig.BOARD_DIMENSION`).

*   **Desplazamiento hacia abajo:** `coordenada + GameConfig.BOARD_DIMENSION`
*   **Desplazamiento hacia arriba:** `coordenada - GameConfig.BOARD_DIMENSION`

*Ejemplo en posicionamiento de barcos vertical:*
```java
// Posicionar un barco verticalmente desde 'row' inicial:
for (int i = 0; i < length; i++) {
    int coord = (row + i) * GameConfig.BOARD_DIMENSION + col;
    board.setCellState(coord, CellState.SHIP);
}
```
