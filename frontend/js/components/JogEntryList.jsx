import React from 'react';
import JogEntry from 'joggr/components/JogEntry';

const JogEntryList = ({ activities, onRemove }) => <div>
  {
    activities.map(activity =>
      <JogEntry activity={activity} key={activity.id} onRemove={onRemove} />
    )
  }
</div>;

JogEntryList.propTypes = {
  activities: React.PropTypes.any,
};

export default JogEntryList;
