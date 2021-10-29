## Aplicación Blogging ##

### ¿Cómo ejecutar localmente? ###

1. Correr el API (desde el directorio principal del proyecto)

```bash
./gradlew bootRun
```

2. Cargar datos externos, ejecutando desde Postman o `curl` el endpoint

```
PUT /admin/api/external
```

Por ejemplo, con `curl`

```bash
curl -X PUT http://localhost:8080/admin/api/external
``` 

3. Instalar dependencias para la interfaz de usuario (esto solo se debe hacer una vez o cada vez que se agreguen nuevas 
   dependencias)
   
```bash
cd ui/
npm install
```

4. Ejecutar el servidor web local para servir la aplicación (interfaz de usuario)

```bash
npm run dev
```

5. Acceder a la aplicación a través de http://localhost:3000
