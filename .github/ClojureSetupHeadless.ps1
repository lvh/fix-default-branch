#!/usr/bin/env pwsh

$ErrorActionPreference = 'Stop'

$Version = '1.10.1.547'
$ClojureToolsUrl = "https://download.clojure.org/install/clojure-tools-$Version.zip"

Write-Host 'Downloading Clojure tools'
[System.Net.ServicePointManager]::SecurityProtocol = [System.Net.SecurityProtocolType]'Tls12'
Invoke-WebRequest -Uri $ClojureToolsUrl -OutFile clojure-tools.zip

Write-Host 'Installing Clojure tools'
$InstallLocations = $env:PSModulePath -split [IO.Path]::PathSeparator
$DestinationPath = $InstallLocations[0]

Write-Host 'Installing PowerShell module'
Expand-Archive clojure-tools.zip -DestinationPath $DestinationPath

Write-Host 'Removing download'
Remove-Item clojure-tools.zip
