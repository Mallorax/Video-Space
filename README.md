# Video-Space
Thus far this app offers:
- display of all kinds of movies provided by api
- letting user clasify movies by assigning them status, this way user can save movies to watch them later, mark as completed etc,
- recommending movies based on movie user is intrested in,
- searching movies within specific genres (there is option to pick multiple as well as to exclude certain genres),
- sorting movies in specific ways(popularity, number of votes, release date, user score)

I've made this project, mainly in order to learn and polish things related to Android Development. Because of this
you may see here certain things, which wouldn't normally fit into this kind of app. So while there will be a lots of mistakes
made mainly due to my own inexperiance, you might also find few things that could feel "forcefully fit in" because I simply wanted to try them out.

Things that I mainly wanted to practise with this project(I might add more later):
- 3 layer architecture (MVVM int his case)
- using REST API via retrofit library,
- android paging library,
- data persistance with room,
- asynchronous programming with Kotlin Coroutines
- dependency injection with Hilt
- material desgin
- instrumented unit tests with espresso and hilt
- standard uninstrumented unit tests

This project is work in progress, so more things will be added later.
If you have any questions or feedback feel free to email me at [patryk733773@gmail.com](mailto:patryk733773@gmail.com?subject=[GitHub]VideoSpace)

## Build
API used in this project is [themoviedb](https://www.themoviedb.org/documentation/api), so if you want to run this app,
you'll need to get you own API key there and put it as apiKey="" in your local.properties file.
