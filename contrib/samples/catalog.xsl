<?xml version="1.0" encoding="UTF-8"?>

<!--
   A simple XSL file from Elliotte Rusty Harold's talk at SD 2000 East
   http://www.ibiblio.org/xml/slides/sd2000east/xslt/
-->

<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:template match="/">
    <html>
       <xsl:apply-templates select="catalog"/>
    </html>
  </xsl:template>

  <xsl:template match="catalog">
    <head>
       <title><xsl:value-of select="category"/></title>      
    </head>
    <body>
       <h1><xsl:value-of select="category"/></h1>      
       <xsl:apply-templates select="composition"/>
       <hr/>
       Copyright <xsl:value-of select="copyright"/><br/>
       Last Modified: <xsl:value-of select="last_updated"/>
    </body>
  </xsl:template>

  <xsl:template match="composition">
    <h3><xsl:value-of select="title"/></h3>

    <ul>
     <li><xsl:value-of select="date"/></li>
     <li><xsl:value-of select="length"/></li>
     <li><xsl:value-of select="instruments"/></li>
     <li><xsl:value-of select="publisher"/></li>    
    </ul>

    <p><xsl:value-of select="description"/></p>    
    
  </xsl:template>

</xsl:stylesheet>
