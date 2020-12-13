try {
    choco -v | Out-Null
} catch {
    Set-ExecutionPolicy Bypass -Scope Process -Force
    [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072
    iex ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'))
}

try {
    java -version
} catch {
    choco install openjdk11
}

try {
    ffmpeg -version | Out-Null
} catch {
    choco install ffmpeg
}

try {
    mvn -v | Out-Null
} catch {
    choco install maven
}

$mvnloc = (Get-Command mvn).Source
cmd /c mklink "mvnlnk.lnk" $mvnloc