package sample.reddit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sample.reddit.model.ChildEntity;
import sample.reddit.model.ChildItem;
import sample.reddit.model.Preview;

import static sample.reddit.util.TimeHelper.getTimeAgo;


public class RedditAdapter extends RecyclerView.Adapter<RedditAdapter.RedditViewHolder> {
    private List<ChildItem> children;
    private Context context;
    private EventListener eventListener;

    public RedditAdapter(Context context, EventListener eventListener) {
        this.context = context;
        this.eventListener = eventListener;
        this.children = new ArrayList<>(0);
    }

    public class RedditViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvAuthor)
        TextView tvAuthor;
        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.tvNumberComments)
        TextView tvNumberComments;
        @BindView(R.id.ivThumbnail)
        ImageView ivThumbnail;


        public RedditViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public RedditViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reddit_itme, parent, false);

        return new RedditViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RedditViewHolder holder, final int position) {
        final ChildItem childItem = children.get(position);
        final ChildEntity child = childItem.getChildren();
        holder.tvTitle.setText(child.getTitle());
        holder.tvAuthor.setText(String.format("%s: %s", context.getString(R.string.author), child.getAuthor()));
        holder.tvDate.setText(getTimeAgo(child.getCreatedUtc()));
        holder.tvNumberComments.setText(String.format("%s: %s", context.getString(R.string.comments), String.valueOf(child.getCommentsNumber())));

        Glide.with(context)
                .load(child.getThumbnail())
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.ivThumbnail);

        holder.ivThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Preview preview = child.getPreview();
                eventListener.onThumbnailClicked(preview);
            }
        });
    }

    public void addItems(List<ChildItem> children) {
        this.children.addAll(children);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return children.size();
    }


    public interface EventListener {
        void onThumbnailClicked(Preview preview);
    }
}
