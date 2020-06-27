#!/usr/bin/env pwsh

# this script is adjusted from the one I got on the wiki[*] because the one on
# the wiki required manual intervention to select an installation destination,
# which of course won't work too well in a headless CI environment :)

# [*]: https://github.com/clojure/tools.deps.alpha/wiki/clj-on-Windows because

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
