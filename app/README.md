# AREP -TALLER DE ARQUITECTURAS DE SERVIDORES DE APLICACIONES, META PROTOCOLOS DE OBJETOS, PATRÓN IOC, REFLEXIÓN
Para este taller los estudiantes deberán construir un servidor Web (tipo Apache) en Java. El servidor debe ser capaz de entregar páginas html e imágenes tipo PNG. Igualmente el servidor debe proveer un framework IoC para la construcción de aplicaciones web a partir de POJOS. Usando el servidor se debe construir una aplicación Web de ejemplo y desplegarlo en Heroku. El servidor debe atender múltiples solicitudes no concurrentes.

## Prerrequisitos
Antes de ejecutar el proyecto es necesario instalar los siguientes programas:
* [Java](https://www.java.com/es/download/ie_manual.jsp). versión 11 o superior.
* [Maven](https://maven.apache.org/).
* [GIT](https://git-scm.com/).
* Version de compilación 1.8 o superior

## Instalación
Para descargar el proyecto primero debemos clonar el repositorio con ayuda de la consola de comandos:
```
git clone https://github.com/JCPosso/Arep-ejercicio.git
```

## Instalación y Compilación

## Instalación
Para descargar el proyecto primero debemos clonar el repositorio con ayuda de la consola de comandos:
```
git clone https://github.com/JCPosso/Arep-ejercicio
```

## Compilación
Para compilar el proyecto usamos Maven en el directorio raiz del proyecto  usando el siguiente comando.
```
mvn compile
mvn package
```
## Ejecución
Para ejecutar el proyecto usamos Maven en el directorio raiz del proyecto  usando el siguiente comando.
```
mvn package
```
Si desea ejecutar el programa desde línea de comandos **(En Windows)** puede usar las siguientes instrucciones:
```
java -cp target/classes;target/dependency/* edu.eci.arep.app.app
```

##Despliegue en HEROKU de manera local
Para el despliegue primero ejecutamos:
```
Heroku local web
```
Una vez iniciado el driver de conexión se ejecutará el servidor web desplegado en heroku para ello nuestra ruta de ejecución será:
```
http://localhost:5000.
```

### App server

![principal](img/principal.PNG)

A continuación se presentan las distintas respuestas generadas por el servidor

- HTML

![html](img/html.PNG)

- CSS

![css](img/css.PNG)

- JS

![javascript](img/javascript.PNG)

- Imagenes

![Imagen](img/imagen.PNG)

## Autor
[Juan Camilo Posso Guevara](https://github.com/JCPosso)
## Derechos de Autor
**©** _Juan Camilo Posso G., Escuela Colombiana de Ingeniería Julio Garavito._
## Licencia
Licencia bajo  [GNU General Public License](https://github.com/JCPosso/none/blob/master/LICENSE).