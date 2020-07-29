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

import java.util.*;


public final class FindMeetingQuery {

  private int getStart(Event event) {
    return event.getWhen().start();
  }

  private int getDuration(Event event) {
    return event.getWhen().duration();
  }

  private int getEnd(Event event) {
    return event.getWhen().end();
  }

  private boolean sameAttendees(Collection<String> a, Collection<String> b) {
    return new HashSet<>(a).removeAll(b);
  }

  private boolean timeOverlaps(int endA, int startB) {
    return endA > startB;
  }

  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {

    Collection<String> newAttendees = request.getAttendees();
    int newDuration = (int) request.getDuration();

    Event [] sortedEvents = events.toArray(new Event[events.size()]);

    boolean [] occupied = new boolean[60 * 24];
    for (int index = 0; index < events.size(); index++) {
      Event current = sortedEvents[index];
      if (sameAttendees(current.getAttendees(), newAttendees)) {
        for (int i = getStart(current); i < getEnd(current); i++) {
          occupied[i] = true;
        }
      }
    }

    List<TimeRange> schedule = new ArrayList<>();

    int start = 0;
    while (start < 60 * 24) {
      if (occupied[start]) {
        start++;
        continue;
      }
      int duration = 1;
      while (start + duration < 60 * 24 && !occupied[start + duration]) {
        duration++;
      }
      if (duration >= newDuration) {
        schedule.add(new TimeRange(start, duration));
      }
      start = duration + start;
    }
    return schedule;
  }
}
