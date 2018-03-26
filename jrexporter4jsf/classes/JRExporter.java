package jrexporter4jsf.classes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import jrexporter4jsf.interfaces.IJReport;
import net.sf.jasperreports.data.DataSourceCollection;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.export.JRXmlExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;

/**
 * JRExporter.java: implements <b>IJReport</b> interfaces, contains simplified methods for exporting datas to PDF/XML/Excel/HTML Formats report using JasperReports Framework.
 * @author MichkaDaCoder
 */
public class JRExporter implements IJReport {

    JasperPrint jasperPrint;

    public JasperPrint getJasperPrint() {
        return jasperPrint;
    }

    public void setJasperPrint(JasperPrint jasperPrint) {
        this.jasperPrint = jasperPrint;
    }

    /**
     * Initialize the report with the Datasource. 
     * (Replace <b>new ArrayList<>();</b> with the collection of fetched datas.)
     *
     * @throws JRException
     */
    @Override
    public void init() throws JRException {
        JRBeanCollectionDataSource jbcd = new JRBeanCollectionDataSource(new ArrayList<>());
        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("YourJasperFile.jasper");
        this.jasperPrint = JasperFillManager.fillReport(path, new HashMap(), new DataSourceCollection(new ArrayList<>()));
    }

    /**
     * Export to a PDF Report File.
     *
     * @param path , path of the binary .jasper file.
     * @throws JRException
     * @throws IOException
     */
    @Override
    public void PDF(String path) throws JRException, IOException {
        init();
        getResponse().addHeader("Content-disposition", "attachment;filename=" + path);
        getResponse().setContentType("application/pdf");
        ServletOutputStream sos = getResponse().getOutputStream();
        JasperExportManager.exportReportToPdfStream(getJasperPrint(), sos);
        getCurrentContext().getResponseComplete();
    }

    /**
     * Export to a XLSX Report File.
     *
     * @param path
     * @throws JRException
     * @throws IOException
     */
    @Override
    public void XLSX(String path) throws JRException, IOException {
        init();
        getResponse().addHeader("Content-disposition", "attachment; filename=" + path);
        ServletOutputStream servletOutputStream = getResponse().getOutputStream();
        JRXlsxExporter docxExporter = new JRXlsxExporter();
        docxExporter.setParameter(JRExporterParameter.JASPER_PRINT, getJasperPrint());
        docxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
        docxExporter.exportReport();
        getCurrentContext().responseComplete();
    }

    /**
     * Gets the current JSF context
     *
     * @return <b>FacesContext</b>
     */
    private FacesContext getCurrentContext() {
        return FacesContext.getCurrentInstance();
    }

    /**
     * Gets a HTTP server response.
     * @return <b>HttpServletResponse</b>
     */
    private HttpServletResponse getResponse() {
        return (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
    }

    /**
     * Export datas to an XML format Report file.
     * @param path
     * @throws JRException
     * @throws IOException
     */
    @Override
    public void XML(String path) throws JRException, IOException {
        init();
        JRXmlExporter exporter = new JRXmlExporter();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, getJasperPrint());
        exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, path);
        exporter.exportReport();
        getCurrentContext().getResponseComplete();
    }

    /**
     * Export datas to an HTML format Report
     *
     * @param path
     * @throws JRException
     * @throws IOException
     */
    @Override
    public void HTML(String path) throws JRException, IOException {
        init();
        getResponse().addHeader("Content-disposition", "attachment;filename=" + path);
        getResponse().setContentType("text/html");
        JasperExportManager.exportReportToHtmlFile(getJasperPrint(), path);
        getCurrentContext().getResponseComplete();
    }
}
