# minesweeper-client

Client of Deviget's minesweeper code challenge.

Developed in groovy, using [Bootique](http://bootique.io) as framework and Jersey-client to make the requests.

Decided to use groovy for its syntax sugar and how easy is to work with maps :) (so no pojos and mappings should be defined/configured).

## how to play

First you need to **register** an account, something like

```shell script
java -jar target/minesweeper-client.jar --register --email "account@mail.com" -nusername -password 'password'
```

will do. This command will greet you and give you a token to start playing.

If you already have an account, you could **login**, in this case you should do something like:

```shell script
java -jar target/minesweeper-client.jar --login --email "account@mail.com" -password 'password'
```

will do. This command also will greet you and give you a token to start playing.

_once you have a token_ you can start interacting with the server. Assuming your token is

`eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4Y2Q5MWJhNS0xMzI5LTRjNzYtOWEzYi1lOGFlZGU3ZTkwZTciLCJpc3MiOiJodHRwczovL21hdGVyby1taW5lc3dlZXBlci5oZXJva3VhcHAuY29tIiwiYXVkIjoiaHR0cHM6Ly9tYXRlcm8tbWluZXN3ZWVwZXIuaGVyb2t1YXBwLmNvbSIsInN1YiI6InJvbWlAbWFpbC5jb20iLCJpYXQiOjE2MDY0NTQyOTcsImV4cCI6MTYwNjQ5ODkzNywicm9sZXMiOlsidXNlciJdLCJyZWZyZXNoQ291bnQiOjAsInJlZnJlc2hMaW1pdCI6MTAwMH0.2ygBF_7opv2Wa9r5i6zQRWo5xPXE0PESkDtkMjd8JaY`

then you can:

- list your games:
```shell script
java -jar target/minesweeper-client.jar  --games \
--token "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4Y2Q5MWJhNS0xMzI5LTRjNzYtOWEzYi1lOGFlZGU3ZTkwZTciLCJpc3MiOiJodHRwczovL21hdGVyby1taW5lc3dlZXBlci5oZXJva3VhcHAuY29tIiwiYXVkIjoiaHR0cHM6Ly9tYXRlcm8tbWluZXN3ZWVwZXIuaGVyb2t1YXBwLmNvbSIsInN1YiI6InJvbWlAbWFpbC5jb20iLCJpYXQiOjE2MDY0NTQyOTcsImV4cCI6MTYwNjQ5ODkzNywicm9sZXMiOlsidXNlciJdLCJyZWZyZXNoQ291bnQiOjAsInJlZnJlc2hMaW1pdCI6MTAwMH0.2ygBF_7opv2Wa9r5i6zQRWo5xPXE0PESkDtkMjd8JaY"
```

- create a game:
```shell script
# create an easy level game
java -jar target/minesweeper-client.jar  --create --easy \
--token "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4Y2Q5MWJhNS0xMzI5LTRjNzYtOWEzYi1lOGFlZGU3ZTkwZTciLCJpc3MiOiJodHRwczovL21hdGVyby1taW5lc3dlZXBlci5oZXJva3VhcHAuY29tIiwiYXVkIjoiaHR0cHM6Ly9tYXRlcm8tbWluZXN3ZWVwZXIuaGVyb2t1YXBwLmNvbSIsInN1YiI6InJvbWlAbWFpbC5jb20iLCJpYXQiOjE2MDY0NTQyOTcsImV4cCI6MTYwNjQ5ODkzNywicm9sZXMiOlsidXNlciJdLCJyZWZyZXNoQ291bnQiOjAsInJlZnJlc2hMaW1pdCI6MTAwMH0.2ygBF_7opv2Wa9r5i6zQRWo5xPXE0PESkDtkMjd8JaY"

# create an intermediate level game
java -jar target/minesweeper-client.jar  --create --intermediate \
--token "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4Y2Q5MWJhNS0xMzI5LTRjNzYtOWEzYi1lOGFlZGU3ZTkwZTciLCJpc3MiOiJodHRwczovL21hdGVyby1taW5lc3dlZXBlci5oZXJva3VhcHAuY29tIiwiYXVkIjoiaHR0cHM6Ly9tYXRlcm8tbWluZXN3ZWVwZXIuaGVyb2t1YXBwLmNvbSIsInN1YiI6InJvbWlAbWFpbC5jb20iLCJpYXQiOjE2MDY0NTQyOTcsImV4cCI6MTYwNjQ5ODkzNywicm9sZXMiOlsidXNlciJdLCJyZWZyZXNoQ291bnQiOjAsInJlZnJlc2hMaW1pdCI6MTAwMH0.2ygBF_7opv2Wa9r5i6zQRWo5xPXE0PESkDtkMjd8JaY"

# create an expert level game
java -jar target/minesweeper-client.jar  --create --expert \
--token "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4Y2Q5MWJhNS0xMzI5LTRjNzYtOWEzYi1lOGFlZGU3ZTkwZTciLCJpc3MiOiJodHRwczovL21hdGVyby1taW5lc3dlZXBlci5oZXJva3VhcHAuY29tIiwiYXVkIjoiaHR0cHM6Ly9tYXRlcm8tbWluZXN3ZWVwZXIuaGVyb2t1YXBwLmNvbSIsInN1YiI6InJvbWlAbWFpbC5jb20iLCJpYXQiOjE2MDY0NTQyOTcsImV4cCI6MTYwNjQ5ODkzNywicm9sZXMiOlsidXNlciJdLCJyZWZyZXNoQ291bnQiOjAsInJlZnJlc2hMaW1pdCI6MTAwMH0.2ygBF_7opv2Wa9r5i6zQRWo5xPXE0PESkDtkMjd8JaY"


# create a custom game
java -jar target/minesweeper-client.jar  --create --custom=15,15,45 \
--token "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4Y2Q5MWJhNS0xMzI5LTRjNzYtOWEzYi1lOGFlZGU3ZTkwZTciLCJpc3MiOiJodHRwczovL21hdGVyby1taW5lc3dlZXBlci5oZXJva3VhcHAuY29tIiwiYXVkIjoiaHR0cHM6Ly9tYXRlcm8tbWluZXN3ZWVwZXIuaGVyb2t1YXBwLmNvbSIsInN1YiI6InJvbWlAbWFpbC5jb20iLCJpYXQiOjE2MDY0NTQyOTcsImV4cCI6MTYwNjQ5ODkzNywicm9sZXMiOlsidXNlciJdLCJyZWZyZXNoQ291bnQiOjAsInJlZnJlc2hMaW1pdCI6MTAwMH0.2ygBF_7opv2Wa9r5i6zQRWo5xPXE0PESkDtkMjd8JaY"
```

- Play games
```shell script
# reveal a cell on a game
java -jar target/minesweeper-client.jar  --reveal --game 3 --row 0 --column 1 \
--token "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4Y2Q5MWJhNS0xMzI5LTRjNzYtOWEzYi1lOGFlZGU3ZTkwZTciLCJpc3MiOiJodHRwczovL21hdGVyby1taW5lc3dlZXBlci5oZXJva3VhcHAuY29tIiwiYXVkIjoiaHR0cHM6Ly9tYXRlcm8tbWluZXN3ZWVwZXIuaGVyb2t1YXBwLmNvbSIsInN1YiI6InJvbWlAbWFpbC5jb20iLCJpYXQiOjE2MDY0NTQyOTcsImV4cCI6MTYwNjQ5ODkzNywicm9sZXMiOlsidXNlciJdLCJyZWZyZXNoQ291bnQiOjAsInJlZnJlc2hMaW1pdCI6MTAwMH0.2ygBF_7opv2Wa9r5i6zQRWo5xPXE0PESkDtkMjd8JaY"

# flag a cell on a game
java -jar target/minesweeper-client.jar  --flag --game 3 --row 0 --column 1 \
--token "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4Y2Q5MWJhNS0xMzI5LTRjNzYtOWEzYi1lOGFlZGU3ZTkwZTciLCJpc3MiOiJodHRwczovL21hdGVyby1taW5lc3dlZXBlci5oZXJva3VhcHAuY29tIiwiYXVkIjoiaHR0cHM6Ly9tYXRlcm8tbWluZXN3ZWVwZXIuaGVyb2t1YXBwLmNvbSIsInN1YiI6InJvbWlAbWFpbC5jb20iLCJpYXQiOjE2MDY0NTQyOTcsImV4cCI6MTYwNjQ5ODkzNywicm9sZXMiOlsidXNlciJdLCJyZWZyZXNoQ291bnQiOjAsInJlZnJlc2hMaW1pdCI6MTAwMH0.2ygBF_7opv2Wa9r5i6zQRWo5xPXE0PESkDtkMjd8JaY"

# unflag a cell on a game
java -jar target/minesweeper-client.jar  --unflag --game 3 --row 0 --column 1 \
--token "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4Y2Q5MWJhNS0xMzI5LTRjNzYtOWEzYi1lOGFlZGU3ZTkwZTciLCJpc3MiOiJodHRwczovL21hdGVyby1taW5lc3dlZXBlci5oZXJva3VhcHAuY29tIiwiYXVkIjoiaHR0cHM6Ly9tYXRlcm8tbWluZXN3ZWVwZXIuaGVyb2t1YXBwLmNvbSIsInN1YiI6InJvbWlAbWFpbC5jb20iLCJpYXQiOjE2MDY0NTQyOTcsImV4cCI6MTYwNjQ5ODkzNywicm9sZXMiOlsidXNlciJdLCJyZWZyZXNoQ291bnQiOjAsInJlZnJlc2hMaW1pdCI6MTAwMH0.2ygBF_7opv2Wa9r5i6zQRWo5xPXE0PESkDtkMjd8JaY"
```

- Pause games
```shell script
java -jar target/minesweeper-client.jar  --pause --game 2 \
--token "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4Y2Q5MWJhNS0xMzI5LTRjNzYtOWEzYi1lOGFlZGU3ZTkwZTciLCJpc3MiOiJodHRwczovL21hdGVyby1taW5lc3dlZXBlci5oZXJva3VhcHAuY29tIiwiYXVkIjoiaHR0cHM6Ly9tYXRlcm8tbWluZXN3ZWVwZXIuaGVyb2t1YXBwLmNvbSIsInN1YiI6InJvbWlAbWFpbC5jb20iLCJpYXQiOjE2MDY0NTQyOTcsImV4cCI6MTYwNjQ5ODkzNywicm9sZXMiOlsidXNlciJdLCJyZWZyZXNoQ291bnQiOjAsInJlZnJlc2hMaW1pdCI6MTAwMH0.2ygBF_7opv2Wa9r5i6zQRWo5xPXE0PESkDtkMjd8JaY"
```
