#Redirection Module
NewL=$(curl -s --location --max-redirs 5 "$1" )
echo $NewL


