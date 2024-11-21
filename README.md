# **NotesRegisterApp**

Este proyecto es una aplicación de registro de notas para Android, que utiliza una base de datos **SQLite** para gestionar usuarios y notas.

---

## 📋 **Descripción**

**NotesRegisterApp** permite a los usuarios:

- **Registrarse** con un nombre de usuario único y una contraseña.
- **Iniciar sesión** con sus credenciales.
- **Crear, guardar y visualizar** notas asociadas a su cuenta.

La aplicación fue desarrollada en **Java** utilizando **Android Studio** y emplea **SQLite** como base de datos local.

---

## 🛠️ **Estructura del Proyecto**

- **`DatabaseHelper.java`**: Clase que gestiona la creación y actualización de la base de datos SQLite.
  - **Tablas**:
    - **`Users`**: Guarda los datos de los usuarios (ID, nombre de usuario, contraseña).
    - **`Notes`**: Guarda las notas creadas por los usuarios (ID, título, contenido, usuario asociado).
- **`LoginActivity.java`**: Pantalla de inicio de sesión y registro.
  - Permite registrar usuarios nuevos y autenticar credenciales.
- **`NotesActivity.java`**: Pantalla principal para gestionar notas.
  - Permite al usuario:
    - **Crear y guardar nuevas notas**.
    - **Visualizar una lista de notas guardadas**.
    - **Regresar al inicio de sesión**.

---

## 🚀 **Funcionalidades**

1. **Registro de Usuarios**:
   - Verifica que el nombre de usuario sea único.
   - Valida que las contraseñas tengan al menos **6 caracteres**.

2. **Inicio de Sesión**:
   - Autenticación de usuarios basada en la base de datos SQLite.

3. **Gestión de Notas**:
   - Los usuarios pueden **crear, guardar y visualizar notas** asociadas a su cuenta.

4. **Regreso al Inicio**:



