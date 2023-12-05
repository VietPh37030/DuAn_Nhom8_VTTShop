package anhpvph37030.fpoly.duan_nhom8.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import anhpvph37030.fpoly.duan_nhom8.R;
import anhpvph37030.fpoly.duan_nhom8.model.MessageModel;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    List<MessageModel>  messageModelList ;

    public MessageAdapter(List<MessageModel> messageModelList) {
        this.messageModelList = messageModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_layout , null);
       ViewHolder holder = new ViewHolder(view);
       return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MessageModel message = messageModelList.get(position);

        if(message.getSentBy().equals(MessageModel.SENT_BY_ME)){

            holder.top_chat_layout.setVisibility(View.VISIBLE);
            holder.top_text.setText(message.getMessage());
            holder.bottom_chat_layout.setVisibility(View.GONE);
            holder.username.setText("Me");
            holder.sener_image.setImageResource(R.drawable.ic_person);

        }else {
            holder.top_chat_layout.setVisibility(View.GONE);
            holder.bottom_tex.setText(message.getMessage());
            holder.bottom_chat_layout.setVisibility(View.VISIBLE);
            holder.username.setText("VTT");
            holder.sener_image.setImageResource(R.drawable.logo);
        }

    }

    @Override
    public int getItemCount() {
        return messageModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout top_chat_layout , bottom_chat_layout;
        TextView top_text , bottom_tex , username;
        ImageView sener_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            top_chat_layout =  itemView.findViewById(R.id.top_chatview);
            bottom_chat_layout =  itemView.findViewById(R.id.bottom_chatview);

            top_text =  itemView.findViewById(R.id.top_text);
            bottom_tex =  itemView.findViewById(R.id.bottom_text);
            username =  itemView.findViewById(R.id.txt_name);
            sener_image =  itemView.findViewById(R.id.profile_image);


        }
    }
}
