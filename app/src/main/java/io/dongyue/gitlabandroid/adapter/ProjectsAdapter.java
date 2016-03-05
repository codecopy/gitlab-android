package io.dongyue.gitlabandroid.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.dongyue.gitlabandroid.R;
import io.dongyue.gitlabandroid.model.api.Project;
import io.dongyue.gitlabandroid.network.GitlabClient;
import io.dongyue.gitlabandroid.view.CircleTransformation;

/**
 * Created by Brotherjing on 2016/3/5.
 */
public class ProjectsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Project> projects;

    public ProjectsAdapter(){
        projects = new ArrayList<>();
    }

    public void add(List<Project> list){
        projects.addAll(list);
        notifyDataSetChanged();
    }

    public void set(List<Project> list){
        projects = list;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ProjectViewHolder.inflate(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Project project = projects.get(position);
        if(holder instanceof ProjectViewHolder){
            ((ProjectViewHolder)holder).bind(project);
        }
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

}
class ProjectViewHolder extends RecyclerView.ViewHolder{

    public static ProjectViewHolder inflate(ViewGroup parent){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_project,parent,false);
        return new ProjectViewHolder(view);
    }

    @Bind(R.id.project_image)ImageView imageView;
    @Bind(R.id.project_title)TextView titleView;
    @Bind(R.id.project_description)TextView descView;

    public ProjectViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void bind(Project project){
        if(project.getAvatarUrl()!=null&&!project.getAvatarUrl().equals(Uri.EMPTY)){
            GitlabClient.getPicasso()
                    .load(project.getAvatarUrl())
                    .transform(new CircleTransformation())
                    .into(imageView);
        }else{
            imageView.setImageResource(android.R.drawable.ic_menu_gallery);
        }
        titleView.setText(project.getNameWithNamespace());
        descView.setText(project.getDescription());
    }
}