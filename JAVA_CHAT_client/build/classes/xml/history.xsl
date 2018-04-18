<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
    <xsl:output method="html"/>
    <xsl:variable name="owner" select="/Chat/Message[1]/Sender/@id"/>
        
    <xsl:template match="/*[1]">
        <html>
            <head>
                <title>
                    <xsl:value-of select="$owner"/>
                </title>
                <title>$owner</title>
                <style type="text/css">
                    <!--  @import url(http://fonts.googleapis.com/css?family=Lato:100,300,400,700);
                    @import url(http://maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css); -->
                    @import url(https://fonts.googleapis.com/css?family=Open+Sans:300,400);
                    

                   
                    /* M E S S A G E S */
                    
                    .container {
                    width: 400px;
                    padding: 10px;
                    }

                    .message-blue {
                    position: relative;
                    margin-left: 20px;
                    margin-bottom: 10px;
                    padding: 10px;
                    background-color: #A8DDFD;
                    width: 200px;
                    height: 50px;
                    text-align: left;
                    font: 400 .9em 'Open Sans', sans-serif;
                    border: 1px solid #97C6E3;
                    border-radius: 10px;
                    }

                    .message-orange {
                    position: relative;
                    margin-bottom: 10px;
                    margin-left: calc(100% - 240px);
                    padding: 10px;
                    background-color: #f8e896;
                    width: 200px;
                    height: 50px;
                    text-align: left;
                    font: 400 .9em 'Open Sans', sans-serif;
                    border: 1px solid #dfd087;
                    border-radius: 10px;
                    }

                    .message-content {
                    padding: 0;
                    margin: 0;
                    }

                    .message-timestamp-right {
                    position: absolute;
                    font-size: .85em;
                    font-weight: 300;
                    bottom: 5px;
                    right: 5px;
                    }

                    .message-timestamp-left {
                    position: absolute;
                    font-size: .85em;
                    font-weight: 300;
                    bottom: 5px;
                    left: 5px;
                    }

                    .message-blue:after {
                    content: '';
                    position: absolute;
                    width: 0;
                    height: 0;
                    border-top: 15px solid #A8DDFD;
                    border-left: 15px solid transparent;
                    border-right: 15px solid transparent;
                    top: -1;
                    left: -15px;
                    }

                    .message-blue:before {
                    content: '';
                    position: absolute;
                    width: 0;
                    height: 0;
                    border-top: 17px solid #97C6E3;
                    border-left: 16px solid transparent;
                    border-right: 16px solid transparent;
                    top: -1px;
                    left: -17px;
                    }

                    .message-orange:after {
                    content: '';
                    position: absolute;
                    width: 0;
                    height: 0;
                    border-bottom: 15px solid #f8e896;
                    border-left: 15px solid transparent;
                    border-right: 15px solid transparent;
                    bottom: 0;
                    right: -15px;
                    }


                    .message-orange:before {
                    content: '';
                    position: absolute;
                    width: 0;
                    height: 0;
                    border-bottom: 17px solid #dfd087;
                    border-left: 16px solid transparent;
                    border-right: 16px solid transparent;
                    bottom: -1px;
                    right: -17px;
                    }   





                   

                </style>
            </head>
            <body>
                           
                <xsl:for-each select="Message">
                    
                    <xsl:choose>
                        <xsl:when test="Sender/@id = $owner">
                                   
                            <div class="container">
                                <div class="message-blue:after"/>
                                <div class="message-blue:before"/>
                                <div class="message-blue">
                                    <p class="message-content">
                                        <xsl:value-of select="Sender"/>
                                                            
                                    </p>
                                    <div class="message-timestamp-left">SMS 13:37</div>
                                </div>
                            </div>
                        </xsl:when>
                        <xsl:otherwise>
                                
                            <div class="container">
                                <div class="message-orange:after"/>
                                <div class="message-orange:before"/>
                                <div class="message-orange">
                                    <p class="message-content">
                                        <xsl:value-of select="Sender"/>
                                    </p>
                                    <div class="message-timestamp-left">SMS 13:37</div>
                                </div>
                            </div>    
                        </xsl:otherwise>
                                
                    </xsl:choose>
                           
                </xsl:for-each>

            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>
