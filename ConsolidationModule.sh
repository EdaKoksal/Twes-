#!/bin/sh
#Consolidation Module for Image Transcoding Service
consolidationGIF()
{	
	GifName1="OutGIF"
	underScore="_"
	GifExtension=".gif"
	LOC='/var/www/html/consolidation'
	total=`ls $LOC/$1GIF*.gif | wc -l`
	totalOut=`expr $total / 10 `
	totalOut=`expr $totalOut \* 10 `

	i=1
	k=0
	m=9
	if [ $total -lt $m ]
	then
		m=$total
		`convert -append $LOC/$1GIF%d.gif[$i-$m] $LOC/$GifName1$1$underScore$k.gif`
		chmod 777 $LOC/$GifName1$1$underScore$k$GifExtension		
	else
		`convert -append $LOC/$1GIF%d.gif[$i-$m] $LOC/$GifName1$1$underScore$k.gif`
		chmod 777 $LOC/$GifName1$1$underScore$k$GifExtension
		k=1
		i=10
		while [ $i -lt $totalOut ]
		do
			m=`expr $i + 9`
			`convert -append $LOC/$1GIF%d.gif[$i-$m] $LOC/$GifName1$1$underScore$k.gif`
			chmod 777 $LOC/$GifName1$1$underScore$k$GifExtension			
			i=`expr $i + 10`
			k=`expr $k + 1`
		done		
		remaining=`expr $total - $totalOut`
		if [ $remaining -gt 0 ] || [ $remaining -eq 0 ]
		then
			`convert -append $LOC/$1GIF%d.gif[$i-$total] $LOC/$GifName1$1$underScore$k.gif`
			chmod 777 $LOC/$GifName1$1$underScore$k$GifExtension
		fi
	fi
	echo "GIF Total:$total"
}

consolidationJPG()
{	
	JpgName1="OutJPG"
	underScore="_"
	JpgExtension=".jpg"
	LOC='/var/www/html/consolidation'
	total=`ls $LOC/$1JPG*.jpg | wc -l`

	totalOut=`expr $total / 10 `
	totalOut=`expr $totalOut \* 10 `

	i=1
	k=0
	m=9
	if [ $total -lt $m ]
	then
		m=$total
		`convert -append $LOC/$1JPG%d.jpg[$i-$m] $LOC/$JpgName1$1$underScore$k.jpg`
		chmod 777 $LOC/$JpgName1$1$underScore$k$JpgExtension		
	else
		`convert -append $LOC/$1JPG%d.jpg[$i-$m] $LOC/$JpgName1$1$underScore$k.jpg`
		chmod 777 $LOC/$JpgName1$1$underScore$k$JpgExtension	
		k=1
		i=10
		while [ $i -lt $totalOut ]
		do
			m=`expr $i + 9`
			`convert -append $LOC/$1JPG%d.jpg[$i-$m] $LOC/$JpgName1$1$underScore$k.jpg`
			chmod 777 $LOC/$JpgName1$1$underScore$k$JpgExtension				
			i=`expr $i + 10`
			k=`expr $k + 1`
		done
		echo "P:$i and T:$total"
		remaining=`expr $total - $totalOut`
		if [ $remaining -gt 0 ] || [ $remaining -eq 0 ]
		then
			`convert -append $LOC/$1JPG%d.jpg[$i-$total] $LOC/$JpgName1$1$underScore$k.jpg`
			chmod 777 $LOC/$JpgName1$1$underScore$k$JpgExtension	
		fi
	fi
	echo "JPG Total:$total"
}

consolidationJPEG()
{	
	JpegName1="OutJPEG"
	underScore="_"
	JpegExtension=".jpeg"
	LOC='/var/www/html/consolidation'
	total=`ls $LOC/$1JPEG*.jpeg | wc -l`
	
	totalOut=`expr $total / 10 `
	totalOut=`expr $totalOut \* 10 `
	echo "name:$1"
	i=1
	k=0
	m=9
	if [ $total -lt $m ]
	then
		m=$total
		`convert -append $LOC/$1JPEG%d.jpeg[$i-$m] $LOC/$JpegName1$1$underScore$k.jpeg`
		chmod 777 $LOC/$JpegName1$1$underScore$k$JpegExtension		
	else
		`convert -append $LOC/$1JPEG%d.jpeg[$i-$m] $LOC/$JpegName1$1$underScore$k.jpeg`
		chmod 777 $LOC/$JpegName1$1$underScore$k$JpegExtension	
		k=1
		i=10
		while [ $i -lt $totalOut ]
		do
			m=`expr $i + 9`
			`convert -append $LOC/$1JPEG%d.jpeg[$i-$m] $LOC/$JpegName1$1$underScore$k.jpeg`
			chmod 777 $LOC/$JpegName1$1$underScore$k$JpegExtension				
			i=`expr $i + 10`
			k=`expr $k + 1`
		done
		echo "P:$i and T:$total"
		remaining=`expr $total - $totalOut`
		if [ $remaining -gt 0 ] || [ $remaining -eq 0 ]
		then
			`convert -append $LOC/$1JPEG%d.jpeg[$i-$total] $LOC/$JpegName1$1$underScore$k.jpeg`
			chmod 777 $LOC/$JpegName1$1$underScore$k$JpegExtension	
		fi
	fi
	echo "JPEG Total:$total"
}

consolidationPNG()
{	
	PngName1="OutPNG"
	underScore="_"
	PngExtension=".png"
	LOC='/var/www/html/consolidation'
	total=`ls $LOC/$1PNG*.png | wc -l`

	totalOut=`expr $total / 10 `
	totalOut=`expr $totalOut \* 10 `

	i=1
	k=0
	m=9
	if [ $total -lt $m ]
	then
		m=$total
		`convert -append $LOC/$1PNG%d.png[$i-$m] $LOC/$PngName1$1$underScore$k.png`
		chmod 777 $LOC/$PngName1$1$underScore$k$PngExtension		
	else
		`convert -append $LOC/$1PNG%d.png[$i-$m] $LOC/$PngName1$1$underScore$k.png`
		chmod 777 $LOC/$PngName1$1$underScore$k$PngExtension	
		k=1
		i=10
		while [ $i -lt $totalOut ]
		do
			m=`expr $i + 9`
			`convert -append $LOC/$1PNG%d.png[$i-$m] $LOC/$PngName1$1$underScore$k.png`
			chmod 777 $LOC/$PngName1$1$underScore$k$PngExtension				
			i=`expr $i + 10`
			k=`expr $k + 1`
		done
		#echo "P:$i and T:$total"
		remaining=`expr $total - $totalOut`
		if [ $remaining -gt 0 ] || [ $remaining -eq 0 ]
		then
			`convert -append $LOC/$1PNG%d.png[$i-$total] $LOC/$PngName1$1$underScore$k.png`
			chmod 777 $LOC/$PngName1$1$underScore$k$PngExtension	
		fi
	fi
	echo "PNG Total:$total"
}

LOC='/var/www/html/consolidation'
chmod 777 $LOC/$1*.*
GifName1="OutGIF"
GifExtension=".gif"
GifName="$GifName1$1$GifExtension"

JpgName1="OutJPG"
JpgExtension=".jpg"
JpgName="$JpgName1$1$JpgExtension"

JpegName1="OutJPEG"
JpegExtension=".jpeg"
JpegName="$JpegName1$1$JpegExtension"

PngName1="OutPNG"
PngExtension=".png"
PngName="$PngName1$1$PngExtension"
totalGIF=`ls $LOC/$1GIF*.gif | wc -l`
totalJPG=`ls $LOC/$1JPG*.jpg | wc -l`
totalJPEG=`ls $LOC/$1JPEG*.jpeg | wc -l`
totalPNG=`ls $LOC/$1PNG*.png | wc -l`

ls -1 $LOC/$1GIF*.gif > /dev/null 2>&1
if [ "$?" = "0" ]; then
	a=$(consolidationGIF $1)
	
fi

ls -1 $LOC/$1JPG*.jpg > /dev/null 2>&1
if [ "$?" = "0" ]; then
	b=$(consolidationJPG $1)
	
fi

ls -1 $LOC/$1JPEG*.jpeg > /dev/null 2>&1
if [ "$?" = "0" ]; then
	c=$(consolidationJPEG $1)
fi

ls -1 $LOC/$1PNG*.png > /dev/null 2>&1
if [ "$?" = "0" ]; then
	d=$(consolidationPNG $1)	
fi



