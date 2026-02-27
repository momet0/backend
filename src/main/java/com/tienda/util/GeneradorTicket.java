package com.tienda.util;

import com.tienda.enums.TipoPromocion;
import com.tienda.models.DetallePedido;
import com.tienda.models.Pedido;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.tienda.models.PromocionProducto;
import org.springframework.stereotype.Component;
import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

@Component // Para poder inyectarla donde la necesites
public class GeneradorTicket {

    // Constantes de Formato
    private static final DecimalFormat MONEDA_FORMAT = new DecimalFormat("$#,##0.00");
    private static final DateTimeFormatter FECHA_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");

    // Constantes de Fuentes
    private static final Font FONT_TITULO = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
    private static final Font FONT_NORMAL = FontFactory.getFont(FontFactory.HELVETICA, 8);
    private static final Font FONT_SUB = FontFactory.getFont(FontFactory.HELVETICA, 7, Font.ITALIC);
    public byte[] generarTicket(Pedido ped) {
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        Document doc = new Document(PageSize.A7, 10, 10, 10, 10);

        try {
            PdfWriter.getInstance(doc, salida);
            doc.open();

            // Cabecera
            doc.add(crearParrafo("TIENDA DE COSMÉTICA", FONT_TITULO, Element.ALIGN_CENTER));
            doc.add(crearParrafo("Ticket de pedido", FONT_NORMAL, Element.ALIGN_CENTER));
            doc.add(crearParrafo("Pedido N°: " + ped.getId() +
                    "\nFecha: " + ped.getFecha().format(FECHA_FORMAT), FONT_NORMAL, Element.ALIGN_LEFT));
            doc.add(new Paragraph("Cliente: " + ped.getCliente() + "\n" + "-".repeat(38), FONT_NORMAL));

            // Tabla de Productos
            PdfPTable tabla = new PdfPTable(new float[]{1, 4, 2});
            tabla.setWidthPercentage(100);

            // Dentro del bucle for de GeneradorTicket
            for (DetallePedido det : ped.getItems()) {
                boolean esPromo = det.getPromocion() != null;
                String nom = esPromo ? "[P] " + det.getPromocion().getNombre() : det.getProducto().getNombre();

                // Fila principal
                tabla.addCell(celda(det.getCantidad() + "x", FONT_NORMAL, 0));
                tabla.addCell(celda(nom, FONT_NORMAL, 0));
                tabla.addCell(celda(MONEDA_FORMAT.format(det.getSubTotal()), FONT_NORMAL, 2));

                if (esPromo) {
                    procesarDetallePromocion(tabla, det);
                }
            }
            doc.add(tabla);

            // Cierre
            doc.add(new Paragraph("-".repeat(38), FONT_NORMAL));
            doc.add(crearParrafo("TOTAL: " + MONEDA_FORMAT.format(ped.getTotal()), FONT_TITULO, Element.ALIGN_RIGHT));
            doc.add(crearParrafo("\n¡Gracias por su compra!", FONT_NORMAL, Element.ALIGN_CENTER));

            doc.close();

        } catch (Exception e) {
            throw new RuntimeException("Error al fabricar el PDF", e);
        }
        return salida.toByteArray();
    }

    // Método auxiliar para limpiar el bucle principal
    private void procesarDetallePromocion (PdfPTable tabla, DetallePedido det){
        TipoPromocion tipo = det.getPromocion().getTipoPromocion();

        if (tipo == TipoPromocion.COMBO) {
            for (PromocionProducto pp : det.getPromocion().getProductosDetalle()) {
                tabla.addCell(celda("", FONT_NORMAL, 0));
                PdfPCell sub = celda(" > " + pp.getCantidad() + "x " + pp.getProducto().getNombre(), FONT_SUB, 0);
                sub.setPaddingLeft(8);
                tabla.addCell(sub);
                tabla.addCell(celda("", FONT_NORMAL, 0));
            }
        } else if (tipo == TipoPromocion.DESCUENTO) {
            tabla.addCell(celda("", FONT_NORMAL, 0));
            String nombreProducto = !det.getPromocion().getProductosDetalle().isEmpty()
                    ? det.getPromocion().getProductosDetalle().get(0).getProducto().getNombre() : "";

            String textoDesc = " * Promo x" + det.getPromocion().getCantidadMinima() + " " + nombreProducto;
            PdfPCell desc = celda(textoDesc, FONT_SUB, 0);
            desc.setPaddingLeft(8);
            tabla.addCell(desc);
            tabla.addCell(celda("", FONT_NORMAL, 0));
        }
    }

    private Paragraph crearParrafo(String t, Font f, int alig) {
        Paragraph p = new Paragraph(t, f);
        p.setAlignment(alig);
        return p;
    }

    private PdfPCell celda(String t, Font f, int alig) {
        PdfPCell c = new PdfPCell(new Phrase(t, f));
        c.setBorder(Rectangle.NO_BORDER);
        c.setHorizontalAlignment(alig);
        return c;
    }
}