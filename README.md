# commitmeter

CommitMeter -- demo service which collect information about all commits in repos
of some organization and show statistics

## Prerequisites

You will need [Leiningen][1] 1.7.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein ring server

## Used technology

  * HTTP Server: Compojure [compojure "1.1.6"]
  * HTTP Client: dakrone/Clj-http [clj-http "0.9.2"]
  * JSON: dakrone/cheshire [cheshire "5.2.0"]

## License

Copyleft © 2014
