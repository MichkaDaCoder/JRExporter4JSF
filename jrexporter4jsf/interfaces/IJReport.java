package jrexporter4jsf.interfaces;

import java.io.IOException;
import net.sf.jasperreports.engine.JRException;

/**
 * IReport.java
 * @author MichkaDaCoder
 */
public interface IJReport {
    void init() throws JRException;
    void PDF(String path) throws JRException, IOException;
    void XLSX(String path) throws JRException, IOException;
    void XML(String path) throws JRException, IOException;
    void HTML(String path) throws JRException, IOException;
}
