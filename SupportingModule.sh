#!/bin/sh
typeControl()
{
	local myGIF='GIF'
	local myJPG='JPG'
	local myJPEG='JPEG'
	local myPNG='PNG'
	local myNONE='NONE'
	
	local gifExtension='.gif'
	local pngExtension='.png'
	local jpgExtension='.jpg'
	local jpegExtension='.jpeg'
	

	local gifName=$1$myGIF$6$gifExtension
	local pngName=$1$myPNG$3$pngExtension
	local jpgName=$1$myJPG$4$jpgExtension
	local jpegName=$1$myJPEG$5$jpegExtension
		
		
	LOCType='/var/www/html/consolidation'
	Type=`identify /var/www/html/consolidation/./$1$2 | cut -f 2 -d " "`
			
	if [ "$Type" = "GIF" ]
	then
		mv $LOCType/./$1$2 $LOCType/./$gifName
		echo "$gifName"
		echo "$myGIF"
	elif [ "$Type" = "JPG" ]
	then
		mv $LOCType/./$1$2 $LOCType/./$jpgName
		echo "$jpgName"
		echo "$myJPG"
	elif [ "$Type" = "JPEG" ]
	then
		mv $LOCType/./$1$2 $LOCType/./$jpegName
		echo "$jpegName"
		echo "$myJPEG"
	elif [ "$Type" = "PNG" ]
	then
		mv $LOCType/./$1$2 $LOCType/./$pngName
		echo "$pngName"
		echo "$myPNG"
	else
		echo "$myNONE"
		echo "$myNONE"
	fi
	
}

checkDimensions()
{
	local onePixel='onePixel'
	local matchingCase='matchingCase'
	local noDataGiven='noDataGiven'
	local resizeNeedsW='resizeNeedsW'
	local resizeNeedsH='resizeNeedsH'
	local anotherTrouble='anotherTrouble'
	LOCDimension='/var/www/html/consolidation'
		
	local indexOne='1'	
	
	if [ "$3" = "$indexOne" ] && [ "$4" = "$indexOne" ] 
	then
		echo "$indexOne"
		echo "$indexOne"		
		echo "$onePixel"
	elif [ "$3" = "$1" ] && [ "$4" = "$2" ] 
	then
		echo "$3"
		echo "$4"
		echo "$matchingCase"
	elif [ "$1" = "0" ] || [ "$2" = "0" ] 
	then
		echo "$3"
		echo "$4"
		echo "$noDataGiven"
	elif ([ ! "$3" = "0" ] && [ ! "$3" = "$1" ]) || ([ ! "$4" = "0" ] && [ ! "$4" = "$2" ]) 
	then
		`convert $LOCDimension/./$5 -resize $1x$2! $LOCDimension/./$5`
		echo "$1"
		echo "$2"
		echo "$resizeNeedsW"
	elif [ "$1" = "" ] || [ "$2" = "" ] 
	then
		echo "$3"
		echo "$4"
		echo "$noDataGivenEmpty"	
	else
		echo "$3"
		echo "$4"
		echo "$anotherTrouble"
	fi
}

ResizeAccordingToPercentage()
{
	LOCDimension='/var/www/html/consolidation'
	`convert $LOCDimension/./$1 -resize $2% $LOCDimension/./$1`
	Width=`identify $LOC/./$1 | cut -f 3 -d " " | sed s/x.*//`
	Heigth=`identify $LOC/./$1 | cut -f 3 -d " " | sed s/.*x//`
	echo "$Width $Heigth"
}

LOC='/var/www/html/consolidation'
FinalWidth=0
FinalHeigth=0
Conclusion="NONE"
NewName="EDA"
Type="NONE"
Value="0"
Value2="1000000"
Value3="1"
Value4="100"
flag=0
DimensionCombinationGIF=""
wget -O "/var/www/html/consolidation/$4$10" "$1"
size=`ls -l $LOC/./$4$10 | cut -f 5 -d " "`

if [ ! "$size" = "$Value" ]
then
	chmod 777 "$LOC/$4$10"	
	TypeCombined=$( typeControl $4 $10 $6 $7 $8 $9)
	NewName=$( echo $TypeCombined | cut -f 1 -d " " )
	Type=$( echo $TypeCombined | cut -f 2 -d " " )	
	if [ ! "$Type" = "NONE" ]
	then		
			Width=`identify $LOC/./$NewName | cut -f 3 -d " " | sed s/x.*//`
			Heigth=`identify $LOC/./$NewName | cut -f 3 -d " " | sed s/.*x//`
			DimensionCombination=$( checkDimensions $2 $3 $Width $Heigth $NewName)
			FinalWidth=$( echo $DimensionCombination | cut -f 1 -d " " )
			FinalHeigth=$( echo $DimensionCombination | cut -f 2 -d " " )
			Dimension=$( echo $DimensionCombination | cut -f 3 -d " " )		
			Conclusion="$NewName and $Type and $Dimension"
			flag=1			
	else 
		FinalWidth=0
		FinalHeigth=0
		Conclusion="NONE"
		Type="NONE"
	fi	
else
	FinalWidth=0
	FinalHeigth=0
	Conclusion="NONE"	
	Type="NONE"		
fi

if [ "$flag" = "$Value3" ] && [ ! "$11" = "$Value4" ]
then
	HWCombination=$( ResizeAccordingToPercentage $NewName $11)
	FinalWidth=$( echo $HWCombination | cut -f 1 -d " " )
	FinalHeigth=$( echo $HWCombination | cut -f 2 -d " " )
	Conclusion=""
	Conclusion="$NewName and $Type and PercentageChange"
	echo $FinalWidth
	echo $FinalHeigth
	echo $Conclusion
	echo "send width:$2 and send h:$3 found W:$Width and found H:$Heigth anddd1 $HWCombination and2 $FinalWidth and3 $FinalHeigth and4"
else
	echo $FinalWidth
	echo $FinalHeigth
	echo $Conclusion
	echo "send width:$2 and send h:$3 found W:$Width and found H:$Heigth AND Size:$size"
fi










