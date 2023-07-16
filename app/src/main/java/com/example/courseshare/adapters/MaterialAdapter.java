package com.example.courseshare.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseshare.models.Material;
import com.example.courseshare.R;
import com.example.courseshare.interfaces.MaterialCallback;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.MaterialViewHolder>
{
    private ArrayList<Material> materials;
    private MaterialCallback materialCallback;

    public MaterialAdapter(ArrayList<Material> materials) {this.materials = materials;}

    @NonNull
    @Override
    public MaterialAdapter.MaterialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.material_item, parent, false);
        MaterialAdapter.MaterialViewHolder materialViewHolder = new MaterialAdapter.MaterialViewHolder(view);
        return materialViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MaterialAdapter.MaterialViewHolder holder, int position)
    {
        Material material = getItem(position);
        holder.material_LBL_materialName.setText(material.getName());
        holder.material_LBL_fileSize.setText("size: " + material.getFileSize() + "KB");
        holder.material_LBL_fileType.setText("file name: "+material.getFileType());
        holder.material_LBL_uploadUser.setText("uploaded by: " + material.getUploadUser());
    }

    @Override
    public int getItemCount() {return this.materials == null ? 0 : this.materials.size();}

    private Material getItem(int position)
    {
        return this.materials.get(position);
    }

    public void updateMaterialList(ArrayList<Material> materials)
    {
        this.materials = materials;
        notifyDataSetChanged();
    }

    public void setMaterialCallback(MaterialCallback materialCallback)
    {
        this.materialCallback = materialCallback;
    }

    public class MaterialViewHolder extends RecyclerView.ViewHolder
    {
        private MaterialTextView material_LBL_materialName;
        private MaterialTextView material_LBL_fileSize;
        private MaterialTextView material_LBL_fileType;
        private MaterialTextView material_LBL_uploadUser;

        public MaterialViewHolder(@NonNull View itemView)
        {
            super(itemView);
            material_LBL_materialName = itemView.findViewById(R.id.material_LBL_materialName);
            material_LBL_fileSize = itemView.findViewById(R.id.material_LBL_fileSize);
            material_LBL_fileType = itemView.findViewById(R.id.material_LBL_fileType);
            material_LBL_uploadUser = itemView.findViewById(R.id.material_LBL_uploadUser);

            itemView.setOnClickListener(v ->
            {
                if(materialCallback!=null)
                    materialCallback.itemClicked(getItem(getAdapterPosition()));
            });
        }
    }
}
