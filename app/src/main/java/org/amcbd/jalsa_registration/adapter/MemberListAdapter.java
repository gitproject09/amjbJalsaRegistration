package org.amcbd.jalsa_registration.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.amcbd.jalsa_registration.R;
import org.amcbd.jalsa_registration.base_url.BaseUrl;
import org.amcbd.jalsa_registration.helper.Helper;
import org.amcbd.jalsa_registration.interfaces.MemberClickListener;
import org.amcbd.jalsa_registration.pojo.MemberListData;

import java.util.List;

public class MemberListAdapter extends RecyclerView.Adapter<MemberListAdapter.MemberListHolder> {

    List<MemberListData> itemList;
    Context context;
    MemberClickListener memberClickListener;

    public MemberListAdapter(Context context, List<MemberListData> itemList, MemberClickListener memberClickListener) {
        this.itemList = itemList;
        this.context = context;
        this.memberClickListener = memberClickListener;
    }

    public void setFriendList(List<MemberListData> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    @Override
    public MemberListHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_list_item, parent, false);

        final MemberListAdapter.MemberListHolder memberListHolder = new MemberListAdapter.MemberListHolder(v);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                memberClickListener.onYesClick(view, memberListHolder.getPosition());
            }
        });
        return memberListHolder;
    }

    @Override
    public void onBindViewHolder(MemberListHolder holder, int position) {

        Helper.loadImageWithCropTransform(BaseUrl.baseUrl + "upload_image/" + itemList.get(position).getProfile_img(),
                R.drawable.mkab_logo, R.drawable.ic_select_photo, holder.ivProfileImage);

        holder.tvFullName.setText("Name : " + itemList.get(position).getName() + " |  ID : " + itemList.get(position).getUser_id());
        holder.tvFatherName.setText("Father Name : " + itemList.get(position).getFather_name());
        holder.tvMotherName.setText("Mother Name : " + itemList.get(position).getMother_name());
        holder.tvMobileNo.setText("Mobile: " + itemList.get(position).getMob() + " |  Blood group : " + itemList.get(position).getBlood_group());


        holder.tvPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                memberClickListener.onNoClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class MemberListHolder extends RecyclerView.ViewHolder {

        ImageView ivProfileImage;
        TextView tvFullName;
        TextView tvFatherName;
        TextView tvMotherName;
        TextView tvMobileNo;
        TextView tvPrint;


        MemberListHolder(View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvFullName = itemView.findViewById(R.id.tvFullName);
            tvFatherName = itemView.findViewById(R.id.tvFatherName);
            tvMotherName = itemView.findViewById(R.id.tvMotherName);
            tvMobileNo = itemView.findViewById(R.id.tvMobileNo);

            tvPrint = itemView.findViewById(R.id.tvPrint);

        }
    }
}
