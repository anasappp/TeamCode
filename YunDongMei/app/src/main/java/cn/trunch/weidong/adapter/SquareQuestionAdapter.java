package cn.trunch.weidong.adapter;

import android.content.Context;
import android.content.Intent;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.trunch.weidong.R;
import cn.trunch.weidong.vo.DiaryUserVO;
import cn.trunch.weidong.activity.SquareQuestionSeeActivity;
import cn.trunch.weidong.entity.UserEntity;
import cn.trunch.weidong.util.TextUtil;
import jp.wasabeef.glide.transformations.CropSquareTransformation;
import jp.wasabeef.glide.transformations.CropTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;


public class SquareQuestionAdapter extends RecyclerView.Adapter<SquareQuestionAdapter.ViewHolder> {

    private Context context;
    private List<DiaryUserVO> questions = new ArrayList<>();

    static class ViewHolder extends RecyclerView.ViewHolder {
        private View questionView;
        private TextView questionTitle;
        private TextView questionContent;
        private ImageView questionPreview;
        private ImageView questionUHead;
        private TextView questionInfo;
        private TextView questionBounty;
        private TextView time;
        public ViewHolder(View view) {
            super(view);
            questionView = view;
            questionTitle = view.findViewById(R.id.questionTitle);
            TextUtil.setBold(questionTitle);
            questionContent = view.findViewById(R.id.questionContent);
            questionPreview = view.findViewById(R.id.questionPreview);
            questionUHead = view.findViewById(R.id.questionUHead);
            questionInfo = view.findViewById(R.id.questionInfo);
            questionBounty = view.findViewById(R.id.questionBounty);
            time = view.findViewById(R.id.time);
        }
    }

    public SquareQuestionAdapter(Context context) {
        this.context = context;
    }

    public void initData(List<DiaryUserVO> questions) {
        if (questions != null) {
            this.questions.clear();
            this.questions.addAll(questions);
            notifyDataSetChanged();
        }
    }

    public void loadData(List<DiaryUserVO> questions) {
        if (questions != null) {
            this.questions.addAll(questions);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_square_question_item, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.questionView.setOnClickListener(v -> {
            DiaryUserVO question = questions.get(viewHolder.getAdapterPosition() - 2);
            Intent questionSeeIntent = new Intent(context, SquareQuestionSeeActivity.class);
            questionSeeIntent.putExtra("did", question.getObjectId());
            questionSeeIntent.putExtra("data",question);
            context.startActivity(questionSeeIntent);
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        DiaryUserVO question = questions.get(i);

        UserEntity user = question.getUser();
        viewHolder.questionTitle.setText(question.getDiaryTitle());
        viewHolder.questionContent.setText(question.getDiaryContent());
        //?????????
        viewHolder.questionPreview.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(question.getDiaryImgPreview())){
            viewHolder.questionPreview.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(R.drawable.bg_image_default)
                    .apply(bitmapTransform(new MultiTransformation<>(
                            new CropSquareTransformation(),
                            new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL)
                    )))
                    .into(viewHolder.questionPreview);
        }else {
            viewHolder.questionPreview.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(question.getDiaryImgPreview())
                    .apply(bitmapTransform(new MultiTransformation<>(
                            new CropSquareTransformation(),
                            new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL)
                    )))
                    .into(viewHolder.questionPreview);
        }
        Glide.with(context)
                .load(user.getuAvatar())
                .apply(bitmapTransform(new CircleCrop()))
                .error(R.drawable.default_head)
                .into(viewHolder.questionUHead);
        viewHolder.questionInfo.setText(user.getuNickname()
                + "    " + question.getDiaryReadNum() + "?????????");
        viewHolder.time.setText(question.getCreatedAt());
        if (question.getDiaryAnonymous() == 0){
            viewHolder.questionBounty.setVisibility(View.INVISIBLE);
        }else {
            viewHolder.questionBounty.setVisibility(View.VISIBLE);
        }
        //viewHolder.questionBounty.setText(question.getDiaryReward() + ".00");
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }
}
