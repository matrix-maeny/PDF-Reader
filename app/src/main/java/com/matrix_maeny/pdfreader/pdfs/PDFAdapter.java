package com.matrix_maeny.pdfreader.pdfs;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.matrix_maeny.pdfreader.DataCentre;
import com.matrix_maeny.pdfreader.PdfViewerActivity;
import com.matrix_maeny.pdfreader.R;

import java.io.IOException;
import java.util.ArrayList;

public class PDFAdapter extends RecyclerView.Adapter<PDFAdapter.viewHolder> {

    Context context;
    ArrayList<PDFModel> list;

    public PDFAdapter(Context context, ArrayList<PDFModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pdf_view_model, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        PDFModel model = list.get(position);
        holder.pdfName.setText(model.getPdfName());
        holder.pdfName.setSelected(true);


        holder.cardView.setOnClickListener(v -> {
            try{
                getPdfText(model);
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    private void getPdfText(PDFModel model) throws IOException {
        PdfReader reader = new PdfReader(model.getPdfPath());

        String resultText = "";
        int n = reader.getNumberOfPages();

        for (int i = 0; i < n; i++) {
            resultText += PdfTextExtractor.getTextFromPage(reader,i+1).trim()+"\n";
        }
        reader.close();

        DataCentre.pdfText = resultText;

        context.startActivity(new Intent(context.getApplicationContext(), PdfViewerActivity.class));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView pdfName;
        ImageView pdfImage;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            pdfName = itemView.findViewById(R.id.pdfName);
            pdfImage = itemView.findViewById(R.id.pdfImage);
        }
    }
}
