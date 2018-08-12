# Oomployees

Aplicación en Android para la gestión de sus Oompa-Loompas. La app consiste básicamente en una lista de trabajadores que puede filtrarse por género y profesión, y un detalle de cada trabajador al que puede accederse haciendo click en el correspondiente elemento de la lista.

## Arquitectura

- Las características principales, la lista y el detalle, están implementadas haciendo uso del patrón MVP, aunque en el caso del adaptador para la lista, posiblemente podría haberse implementado un presenter específico para éste. El filtro de la lista también implementa el patrón MVP. El objeto filtro lo he inyectado como dependencia.
- Cada una de éstas está organizado en sus correspondientes packages: com.alexbarcelo.oomployees.oompaList y [...].oompaDetail.
- Dado que el servidor no ofrece información de cache, se ha añadido un interceptor al cliente REST para dar 1 minuto de validez a los datos descargados.
- En cuanto a los patrones para el modelo: He hechlo una implementación sencilla en la que hay una interficie para obtener los datos, que es con la que trabaja el presenter, y que tiene una implementación que trabaja directamente con el cliente REST y otra que mockea los datos. 
- Al no tener dominio de los test instrumentales, he hecho uso de una implementación de IdlingResource, ElapsedTimeIdlingResource, by Chiu-Ki Chan (https://github.com/chiuki).
- El presenter del detalle contiene referencias a clases de Android, a pesar de que se recomienda que no sea así. Añadir el contexto era necesario para obtener la imágen via Glide. Incluir este proceso en el fragmento también rompería la máxima de hacer la vista lo más tonta posible. Seguramente una mejor implementación sería añadir una clase auxiliar a la que inyectar las dependencias de Android, en lugar de hacerlo sobre el presenter.
- He tenido varias dudas sobre cómo implementar algunas de las partes, y en algunos casos, dada la limitación de tiempo para la realización de esta aplicación, he optado por la opción más sencilla en lugar de la más "correcta". Precisamente, una de las cosas que espero encontrar en un entorno de programación en Android es poder discutir de estas cuestiones en grupo, en lugar de mantener un debate monopersonal.
- La propuesta cromática puede no gustar a diseñadores, pero tiene el beneplácito de Willy Wonka.

## Librerías de terceros

- <b>AutoValue: </b> Permite la creación de clases inmutables mediante anotación de la clase base. Implementa los métodos de hashCode, equals y toString, además de simplificar el código de la clase. He incluido también la extensión hecha por Ryan Harter para que la librería Gson (la que he empleado para manejar JSON en el cliente REST) pueda trabajar cómodamente con clases anotadas con AutoValue.
- <b>RxJava 2: </b> A pesar de no hacer un uso intensivo de este framework, lo he usado al ver que es hoy por hoy un standard en la programación en Android, y mi idea es incorporarlo en todos mis proyectos para poder profundizar en las características que ofrece.
- <b>Retrofit 2: </b>Simplifica la creación de clientes REST para Android.
- <b>Dagger 2: </b>Estándar de facto para la inyección de dependencias en Android. Tal vez no sea indispensable para una app de estas dimensiones, pero no deja de resultar útil.
- <b>Butterknife: </b>Entre otras características, permite simplificar la asignación de vistas a variables (a través del método findViewById). Hace bastante más limpio los bloques de código correspondientes a los eventos de creación de fragments o activities.
- <b>Mockito: </b>Otro estándar, muy útil para los tests de presenter.
- <b>Glide: </b>A pesar de no haber probado de forma extensiva los distintos frameworks que permite la descarga de imágenes en Android, he escogido ésta ya que es la que se menciona en la documentación oficial de Google.
