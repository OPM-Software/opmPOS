# Sistema Control de Inventario y Punto de Venta
Sistema Control de Inventarios y PDV

## Prerequisitos

 - Java 1.8 
 - Maven 3.6.3


## Para Colaboradores.

Como crear Fork y Hacer pull Request al proyecto

https://www.youtube.com/watch?v=3m7Z3g_U-Cs

### Instalación

Una vez clonado el proyecto tendras que instalar en tu repositorio local de maven el jar que se encuentran en la carpeta complementos.  
1.- Abre una terminal ubicandote en la carpeta /complementos y ejecuta el siguiente comando.

     
     mvn install:install-file -Dfile=hsqldb-jdk8.jar -DgroupId=org.hsqldb -DartifactId=hsqldb-jdk8 -Dversion=1.0.0 -Dpackaging=jar
     

2.- Para ejecutar la aplicacion es necesario pasar un argumento con la direccion en donde ubiques la base de datos sysman.accdb que se encuentra en la carpeta complementos.

Ejemplo

    -DurlDB="C:\opmPOS\sysman.accdb"
   
En eclipse seria de la siguiente manera
    ![image](https://user-images.githubusercontent.com/24512671/124346593-67a6bf00-dba5-11eb-9fa3-67f8f2433960.png)


## Login

![alt text](https://github.com/Open-Source-Mexico/opmPOS/blob/master/PDV/Login.png?raw=true)

## Ventas

![alt text](https://github.com/Open-Source-Mexico/opmPOS/blob/master/PDV/Home.png?raw=true)

### Usuarios de Sesion

  - Usuario: admin
    - Contraseña:123
  
  - Usuario: cajero
    - Contraseña: 456
