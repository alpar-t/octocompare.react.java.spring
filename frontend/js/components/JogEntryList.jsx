import React from 'react';
import JogEntry from 'octocompare/components/JogEntry';

const JogEntryList = ({ activities, onRemove, onAddOrEdit }) => <div>
  {
    activities.map(activity =>
      <JogEntry
        activity={activity}
        key={activity.id}
        onRemove={onRemove}
        onAddOrEdit={onAddOrEdit}
      />
    )
  }
</div>;

JogEntryList.propTypes = {
  activities: React.PropTypes.shape({
  }),
  onAddOrEdit: React.PropTypes.func,
  onRemove: React.PropTypes.func,
};

export default JogEntryList;
