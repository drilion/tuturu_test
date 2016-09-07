package com.selfcompany.tuturutest.support;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.selfcompany.tuturutest.R;
import com.selfcompany.tuturutest.subjects.City;
import com.selfcompany.tuturutest.subjects.Station;

import java.util.List;

/**
 * Created by Sergey on 06.09.2016.
 */
public class MyExpandableRecyclerViewAdapter extends ExpandableRecyclerAdapter
        <MyExpandableRecyclerViewAdapter.CountryCityViewHolder,
        MyExpandableRecyclerViewAdapter.ExpandedViewHolder> {

    private LayoutInflater layoutInflater;

    //слушатель кликов и долгих нажатий на элементы развернутого списка
    private ItemClickListener itemClickListener;

    public interface ItemClickListener {

        void onClickItem(int id);

        void onLongClickItem(int id);

    }

    public MyExpandableRecyclerViewAdapter(Context context, List<? extends ParentListItem> parentItemList, ItemClickListener itemClickListener) {
        super(parentItemList);
        layoutInflater = LayoutInflater.from(context);
        this.itemClickListener = itemClickListener;
    }
    //разворачиваемый элемент (Страна, город)
    @Override
    public CountryCityViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View rootView = layoutInflater.inflate(R.layout.item_expandable_recycler_view, parentViewGroup, false);
        return new CountryCityViewHolder(rootView);
    }
    //элемент развернутого списка (станция)
    @Override
    public ExpandedViewHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        View expanderViewHolder = layoutInflater.inflate(R.layout.item_expanded_recycler_view_station, childViewGroup, false);
        return new ExpandedViewHolder(expanderViewHolder);
    }

    @Override
    public void onBindParentViewHolder(CountryCityViewHolder parentViewHolder, int position, ParentListItem parentListItem) {
        parentViewHolder.bind((City) parentListItem);
    }

    @Override
    public void onBindChildViewHolder(ExpandedViewHolder childViewHolder, int position, Object childListItem) {

        childViewHolder.bind((Station) childListItem);
    }

    class CountryCityViewHolder extends ParentViewHolder {

        private TextView countryCityTextView;
        private TextView childCount;
        private ImageView expander;

        public CountryCityViewHolder(View itemView) {
            super(itemView);
            countryCityTextView = (TextView) itemView.findViewById(R.id.item_expandable_country_city);
            childCount = (TextView) itemView.findViewById(R.id.item_expandable_content_count);
            expander = (ImageView) itemView.findViewById(R.id.item_expandable_expander);
        }

        public void bind(City city) {
            countryCityTextView.setText(layoutInflater.getContext().getString(R.string.item_station_details,
                    city.getCountryTitle(), city.getCityTitle()));
            childCount.setText(Integer.toString(city.getChildItemList().size()));
            expander.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isExpanded()) {
                        collapseView();
                    } else {
                        expandView();
                    }
                }
            });
        }

        @Override
        public boolean shouldItemViewClickToggleExpansion() {
            return false;
        }

        @Override
        public void onExpansionToggled(final boolean expanded) {
            super.onExpansionToggled(expanded);
            final Animation animationRotate = AnimationUtils.loadAnimation(layoutInflater.getContext(), R.anim.rotate_expander);
            if (expanded) {
                animationRotate.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        expander.setImageResource(R.drawable.ic_keyboard_arrow_down_black_48dp);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });
            } else {
                animationRotate.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        expander.setImageResource(R.drawable.ic_keyboard_arrow_up_black_48dp);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });
            }
            expander.startAnimation(animationRotate);
        }
    }

    class ExpandedViewHolder extends ChildViewHolder implements View.OnClickListener, View.OnLongClickListener{

        private TextView stationTitle;
        private TextView stationDetails;
        private int stationId;

        public ExpandedViewHolder(View view) {
            super(view);
            stationTitle = (TextView) view.findViewById(R.id.item_station_title);
            stationDetails = (TextView) view.findViewById(R.id.item_station_details);
        }

        public void bind(Station station) {
            stationId = station.getStationId();
            stationTitle.setText(station.getStationTitle());
            stationDetails.setText(layoutInflater.getContext().getString(R.string.item_station_details,
                    station.getCountryTitle(),
                    station.getCityTitle()));
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClickItem(stationId);
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onLongClickItem(stationId);
            return true;
        }
    }
}
