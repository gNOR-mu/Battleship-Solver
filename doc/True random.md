# True random

# Descripción general

Uno de los algoritmos más sencillos e ineficientes posibles, consiste solamente en el azar.

# Funcionamiento

Elije una coordenada al azar para su disparo.


# Variaciones
Implementados 
- [x] True Random sin memoria
- [ ] True Random con memoria
- [ ] True Random con memoria y estado

# True Random sin memoria

No tiene memoria, no recuerda sus disparos anteriores.


# True Random con memoria

Tiene memoria, recuerda sus disparos anteriores, pero no recuerda el resultado de los disparos.

Se utiliza una estrategia de variación de Fisher-Yates para elegir una celda al azar no disparada:

1. Selecciona un indice aleatorio de una celda no disparada
2. Intercambia la posición en el arreglo de la celda seleccionada con la última celda no disparada
3. Decrementa la cantidad de celdas no disparadas
4. Repite hasta que no queden celdas no disparadas

# True Random con memoria y estado

Tiene memoria, recuerda sus disparos anteriores y el resultado de los disparos.
