@rem ============================================================
@rem Script para remover e recriar o container do Gestao BSI
@rem com uma nova imagem
@rem ============================================================

@echo off

@rem ============================================================
@rem Definicao das variaveis de ambiente
@rem ============================================================
set IMAGE_NAME=gestao-bsi-ktor-docker-image
set CONTAINER_NAME=gestao-bsi-ktor

@rem ============================================================
@rem Para o container
@rem ============================================================

echo.
echo [1] Parando o container %CONTAINER_NAME%
docker stop %CONTAINER_NAME%

@rem ============================================================
@rem Remove o container
@rem ============================================================

echo.
echo [2] Removendo o container %CONTAINER_NAME%
docker rm %CONTAINER_NAME%

@rem ============================================================
@rem Remove a imagem
@rem ============================================================

echo.
echo [3] Removendo a imagem %IMAGE_NAME%
set IMAGE_ID=
for /F "tokens=*" %%i in ('docker images --format "{{.ID}}" %IMAGE_NAME%') DO set IMAGE_ID=%%i
if defined IMAGE_ID docker rmi -f %IMAGE_ID%

@rem ============================================================
@rem Carrega a nova imagem
@rem ============================================================

echo.
echo [4] Carregando a nova imagem
docker load -i build/jib-image.tar

@rem ============================================================
@rem Verifica se a imagem foi carregada
@rem ============================================================

docker images --format "{{.Repository}}" | findstr /I "%IMAGE_NAME%" >nul

if %ERRORLEVEL%==0 (
echo.
echo [OK] A imagem "%IMAGE_NAME%" foi carregada!
) else (
echo.
echo [ERRO] A imagem "%IMAGE_NAME%" NAO foi carregada!
goto :fim
)

@rem ============================================================
@rem Cria o novo container: gestao-bsi-ktor
@rem
@rem Rede virtual: postgresql-network
@rem Container do PostgreSQL: postgresql-db
@rem Variáveis de ambiente para produção:
@rem DB_URL=jdbc:postgresql://postgresql-db:5432/BSI
@rem APP_PORT=8080
@rem ============================================================

echo.
echo [5] Executando o container %CONTAINER_NAME%

docker run -d -p 8080:8080 --name %CONTAINER_NAME% --network  postgresql-network  -e DB_URL=jdbc:postgresql://postgresql-db:5432/BSI -e APP_PORT=8080 -e TZ=America/Sao_Paulo -d gestao-bsi-ktor-docker-image:1.0.1

@rem ============================================================
@rem Verifica se o container está rodando
@rem ============================================================

docker ps --format "{{.Names}}" | findstr /I "%CONTAINER_NAME%" >nul

if %ERRORLEVEL%==0 (
echo.
echo [OK] O container "%CONTAINER_NAME%" esta rodando!
) else (
echo.
echo [ERRO] O container "%CONTAINER_NAME%" NAO esta rodando!
)

:fim