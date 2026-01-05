@echo off
title Setup Python
color 0A

echo ==================================================
echo     Python scripts setup for StateWallet
echo ==================================================
echo.

echo [INFO] Checking if Python is installed
REM === Cheching if Python is installed ===
python --version >nul 2>&1
IF ERRORLEVEL 1 (
    echo [ERROR] Python is not installed
    echo Download latest version bellow:
    echo https://www.python.org/downloads/
    pause
    exit /b
)

echo [INFO] Python was found!
python --version
echo.

REM === Downloading essential libraries for Python scripts ===
echo [INFO] Downloading essential libraries...
python -m pip install --upgrade pip
python -m pip install PyMuPDF
python -m pip install google-genai

echo.
echo [OK] The Python script is ready to run!
echo.
