// Copyright (c) 2023 Irene Martirosyan, Patrick Telfer, Stephen Cao, Tam Nguyen

package net.codebot.application.presentation

import com.gluonhq.richtextarea.model.ParagraphDecoration
import com.gluonhq.richtextarea.model.TextDecoration
import javafx.stage.FileChooser
import javafx.stage.Stage
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document as iDoc
import com.itextpdf.layout.element.*
import com.itextpdf.layout.properties.TextAlignment as iTextAlignment
import com.itextpdf.layout.element.List as iList
import com.itextpdf.kernel.colors.DeviceRgb
import javafx.scene.text.FontPosture
import javafx.scene.text.FontWeight
import javafx.scene.text.TextAlignment
import java.io.File
import java.io.FileOutputStream

class FileManager (val noteListView: NoteListView) {
    private val chooser = FileChooser()
    private val pdfOpt = FileChooser.ExtensionFilter("pdf", "*.pdf")
    private val txtOpt = FileChooser.ExtensionFilter("text", "*.txt")

    init {
        chooser.extensionFilters.addAll(pdfOpt, txtOpt)
        chooser.title = "Export note"
    }

    public fun showFileChooser(stage: Stage) {
        // Kinda unsafe
        val currentNote = noteListView.thisNoteView as NoteView
        chooser.initialFileName = currentNote.noteTitle.text.replace(" ", "")
        val file = chooser.showSaveDialog(stage) ?: return
        if (chooser.selectedExtensionFilter == txtOpt) {
            writeTxt(file, currentNote)
        } else {
            writePdf(file, currentNote)
        }
    }

    private fun writeTxt(file: File, currentNote: NoteView) {
        val fos = FileOutputStream(file)
        if (!file.exists()) {
            file.createNewFile()
        }
        fos.write(currentNote.editor.getContent().text.toByteArray())
        fos.flush()
        fos.close()
    }

    private fun writePdf(file: File, currentNote: NoteView) {
        val result = getPdfElements(currentNote)
        if (result.isEmpty()) {
            return
        }
        chooser.initialDirectory = file.parentFile
        val w = PdfWriter(file)
        val pdfDoc = PdfDocument(w)
        val doc = iDoc(pdfDoc)
        for (para in result) {
            doc.add(para)
        }
        doc.close()
        w.close()
    }

    private fun getPdfElements(currentNote: NoteView) : List<IBlockElement> {
        // keep adding to a paragraph
        // when change alignment, finish last paragraph, then start new paragraph
        val result = ArrayList<IBlockElement>()
        val currentDoc = currentNote.editor.getContent()

        try {
            val text = currentDoc.text
            val decors = currentDoc.decorations
            var lastAlignment = TextAlignment.LEFT
            var para = Paragraph()
            for (dec in decors) {
                val decEnd = dec.start + dec.length
                val part = text.substring(dec.start, decEnd)
                // Kinda unsafe but should be OK since we don't use ImageDecoration
                val tDec: TextDecoration? = dec.decoration as? TextDecoration
                if (tDec == null) {
                    // Error casting. Should not happen if we don't have images
                    throw Exception("error not TextDecoration")
                }
                val curAlignment = dec.paragraphDecoration.alignment
                if (result.isEmpty() && para.isEmpty) {
                    lastAlignment = curAlignment
                }

                val ele = Text(part)

                // TODO: font family
                // We don't have many fonts in itextpdf
                // We have to create font, like with .tff file
                // We can use itextpdf default fonts for simplicity
                // val font = tDec.fontFamily

                val color = tDec.foreground
                ele.setFontColor(DeviceRgb(color.red.toFloat(), color.green.toFloat(), color.blue.toFloat()))

                val size = tDec.fontSize.toFloat()
                ele.setFontSize(size)

                if (tDec.fontPosture == FontPosture.ITALIC) {
                    ele.setItalic()
                }
                if (tDec.fontWeight == FontWeight.BOLD) {
                    ele.setBold()
                }
                if (tDec.isUnderline) {
                    ele.setUnderline()
                }
                if (tDec.isStrikethrough) {
                    ele.setLineThrough()
                }

                // TODO: numerical and bulleted list

                if (curAlignment != lastAlignment) {
                    // Finish paragraph if different alignment than last one
                    para.setTextAlignment(translateAlignment(lastAlignment))

                    result.add(para)

                    lastAlignment = curAlignment
                    para = Paragraph()
                }
                // Add to current paragraph
                para.add(ele)
                para.add(iList())
            }

            if (!para.isEmpty) {
                para.setTextAlignment(translateAlignment(lastAlignment))
                result.add(para)
            }
        } catch (e: Exception) {
            println(e)
            result.clear()
            result.add(Paragraph(""))
        }
        return result
    }

    private fun translateAlignment(alignment: TextAlignment): iTextAlignment {
        // Assume we only use left, center, and right alignments
        return when (alignment) {
            TextAlignment.LEFT -> iTextAlignment.LEFT
            TextAlignment.CENTER -> iTextAlignment.CENTER
            else -> iTextAlignment.RIGHT
        }
    }
}
