# Brute Force

# Descripción general

Utiliza la fuerza bruta para resolver el tablero, recorriéndolo de izquierda a derecha y de arriba abajo para resolverlo.

- De forma predeterminada ignora cualquier resultado de disparos anterior.
- Una variación sobre este algoritmos es una versión más "inteligente", considerando el resultado del último disparo para evaluar si debe disparar a las casillas adyacentes.

# Funcionamiento

Empieza con un índice (idx) establecido en 0, el cual representa el número de la casilla a disparar considerando las dimensiones del tablero (BOARD_DIMENSION).

Para obtener las coordendadas del próximo disparo:
    - Fila: idx / BOARD_DIMENSION
    - Columna: idx % BOARD_DIMENSION