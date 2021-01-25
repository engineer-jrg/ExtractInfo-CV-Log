/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bancolombia.extractinfo.cv.log.utils;

import com.bancolombia.extractinfo.cv.log.MainWindow;
import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

/**
 *
 * @author EQUIPO
 */
public class ExecutionInformation {
    
    private final SimpleDateFormat objSDF = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss.SSS");
    
    // Constructor
    public ExecutionInformation() {
    }

    /*
    * Imprime en consola la configuracion del pom.xml con que se compilo el artefacto..
    * @return null.
     */
    public void printInfoBuild(Model model) {
        System.out.println("");
        System.out.println("***** Informacion de compilacion *****");
        System.out.println("Id: ---------------- " + model.getId());
        System.out.println("GroupId: ----------- " + model.getGroupId());
        System.out.println("ArtifactId: -------- " + model.getArtifactId());
        System.out.println("Name: -------------- " + model.getName());
        System.out.println("Description: ------- " + model.getDescription());
        System.out.println("Version: ----------- " + model.getVersion());
        System.out.println("Url: --------------- " + model.getUrl());
        System.out.println("Execution date: ---- " + this.objSDF.format(new Date()));
        System.out.println("**************************************");
    }
    
    /*
    * Imprime en consola las propiedades con las que se ejecuta el artefacto.
    * @return null.
     */
    public void printInfoProperties(String sysEnv) {
        try (InputStream input = MainWindow.class.getClassLoader().getResourceAsStream(sysEnv + ".properties")) {
            Properties prop = new Properties();
            if (input != null) {
                prop.load(input);
                Set<String> propKeys = prop.stringPropertyNames();
                if(propKeys.size()>0){
                   System.out.println("");
                    System.out.println("****** Propiedades de ejecucion ******");
                    for (String propKey : propKeys) {
                        System.out.println(propKey + ": ----------- " + prop.getProperty(propKey));
                    }
                    System.out.println("**************************************"); 
                }else{
                    System.out.println("No hay propiedades definidas"); 
                }
            }
        } catch (IOException ex) {
            // ex.printStackTrace();
            this.showMessage("Error leyendo las propiedades" + ex.getMessage(), 2, new JLabel());
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
    * Muestra informacion de los procesos, errores o advertencias que sucedan durante la ejecucion de la aplicacion.
    * @return null
     */
    public void showMessage(String message, int type, JLabel label) {
        switch (type) {
            case 2:
                JOptionPane.showMessageDialog(null, message, "Mensaje de error", JOptionPane.ERROR_MESSAGE);
                label.setForeground(Color.RED);
                break;
            case 3:
                label.setForeground(Color.YELLOW);
                break;
            default:
                label.setForeground(Color.BLACK);
        }
        label.setText(message);
    }
    
    /*
    * Muestra informacion de los procesos, errores o advertencias que sucedan durante la ejecucion de la aplicacion
    * por la consola.
    * @return null
     */
    public void showMessage(String message, boolean debug) {
        if(debug) System.out.println(this.objSDF.format(new Date()) + " ::: " +message);
    }
    
    /*
    * Obtiene la configuracion del pom.xml con que se compilo el artefacto.
    * @return boolean - True si obtuvo la configuracion.
     */
    public Model getInfoBuild() throws FileNotFoundException, IOException, XmlPullParserException {
        MavenXpp3Reader reader =  new MavenXpp3Reader();
        InputStream pomFile = MainWindow.class.getClassLoader().getResourceAsStream("files/effective-pom.xml");
        Model model = reader.read(pomFile);
        return model;
    }
}
