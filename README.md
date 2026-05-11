# NumberSums 
## Se trata de una matriz cuadrada de números, donde solo algunos se seleccionan para cumplir las sumas de las filas y las columnas.

### Acceso rápido
[Acceso a la resolución visual del problema](#visualización-problema).

[Acceso a la resolución técnica del problema](#resolución-problema).

[Acceso a las localizaciones de archivos del problema](#localizaciones-de-archivos-del-problema).

## Visualización problema
Ejemplo: Problema
````
7 9 6 | 13
3 5 1 | 5
6 9 7 | 7
------
7 5 13 
````

La respuesta sería seleccionar los valores de las filas y las columnas para que las sumas correspondan.

````
7 . 6 | 13
. 5 . |  5
. . 7 |  7
------
7 5 13 
````
## Resolución problema
El tablero del ejemplo se escribe:    
````
7 9 6 13 ; 3 5 1 5 ; 6 9 7 7 ; 7 5 13 
````
Para el resultado, escribir el mismo tablero, sustituyendo los valores que sobran por puntos.
````
7 . 6 13 ; . 5 . 5 ; . . 7 7 ; 7 5 13
````
## Localizaciones de archivos del problema
El fichero tableros.txt encontrado en el src son los problemas propuestos por el profesor en la página [tableros.txt](http://opendatalab.uhu.es/aplicaciones/2026si/descargas/tableros.txt)
> El fichero soluciones.txt contiene las soluciones a estos problemas
