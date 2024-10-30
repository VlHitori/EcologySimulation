 #!/bin/bash                                                                                                                                                                                                                        
                                                                                                                                                                                                                                     
mvn clean install || { echo "Maven build failed"; exit 1; }                                                                                                                                                                        
                                                                                                                                                                                                                                     
JAR_FILE=$(find target -name "*.jar" | head -n 1)                                                                                                                                                                                  
                                                                                                                                                                                                                                     
if [[ -z "$JAR_FILE" ]]; then                                                                                                                                                                                                      
   echo "JAR file not found in target/"                                                                                                                                                                                           
   exit 1                                                                                                                                                                                                                         
fi                                                                                                                                                                                                                                 
                                                                                                                                                                                                                                    
java -cp "$JAR_FILE" com.cabachok.App
