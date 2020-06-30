// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Arrays;

public final class FindMeetingQuery {
  /**
   * Finds a collection of time range that the meeting can be hold
   */
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    // Sort the events list based the starting time of each event
    Event[] eventsArray = events.toArray(new Event[0]);
    Arrays.sort(eventsArray, ORDER_BY_TIME_RANGE);

    // Find the collection of time range to hold the meeting
    Collection<String> requiredAttendees = request.getAttendees();
    Collection<TimeRange> queries = new ArrayList<>();
    int preStartTime = TimeRange.START_OF_DAY;
    for (Event event : eventsArray) {
      TimeRange eventTimeRange = event.getWhen();
      if (!Collections.disjoint(event.getAttendees(), requiredAttendees)) {
        if (canRequestBeHold(preStartTime, eventTimeRange.start(), request)) {
          queries.add(TimeRange.fromStartEnd(preStartTime, eventTimeRange.start(), false));
        }
        // Update the preStartTime to point next possible time to hold the meeting
        int eventEndTime = eventTimeRange.end();
        preStartTime = eventEndTime > preStartTime ? eventEndTime : preStartTime;
      }
    }
    // Take care the END_OF_DAY to see if the meeting can be hold
    if (canRequestBeHold(preStartTime, TimeRange.END_OF_DAY, request)) {
      queries.add(TimeRange.fromStartEnd(preStartTime, TimeRange.END_OF_DAY, true));
    }
    return queries;
  }
  
  /**
   * Returns true if the meeting request can be hold during the start time and end time,
   * false otherwise.
   */
  private static boolean canRequestBeHold(int startTime, int endTime, MeetingRequest request) {
    return endTime - startTime >= request.getDuration();
  }

  /**
   * A comparator for sorting events by their starting time in ascending order.
   */
  private static final Comparator<Event> ORDER_BY_TIME_RANGE = new Comparator<Event> () {
    @Override
    public int compare(Event a, Event b) {
      return TimeRange.ORDER_BY_START.compare(a.getWhen(), b.getWhen());
    }
  };
}
