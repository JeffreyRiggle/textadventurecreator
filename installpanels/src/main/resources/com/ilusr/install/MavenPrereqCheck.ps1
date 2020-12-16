try {
    mvn -v | Out-Null
    $result = $?
    
    if ( $result ) {
        exit 0
    } else {
        exit 1
    }
} catch {
    exit 1
}
