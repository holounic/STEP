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

  private int getEnd(Event event) {
    return event.getWhen().end();
  }

  private boolean sameAttendees(Collection<String> a, Collection<String> b) {
    return new HashSet<>(a).removeAll(b);
  }

  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {

    Collection<String> attendees = request.getAttendees();
    int duration = (int) request.getDuration();
    int [] occupied = new int[60 * 24 + 1];

    for (Event event : events) {
      if (sameAttendees(event.getAttendees(), attendees)) {
        occupied[getStart(event)] += 1;
        occupied[getEnd(event)] -= 1;
      }
    }

    List<TimeRange> schedule = new ArrayList<>();

    int index = 0;
    int balance = 0;
    while (index < 60 * 24) {
      int start = index;

      while (index < 60 * 24 && occupied[index] < 1) {
        index++;
      }
      if (duration <= index - start) {
        schedule.add(new TimeRange(start, index - start));
      }

      while (index < 60 * 24) {
        balance += occupied[index];
        if (balance == 0) {
          break;
        }
        index++;
      }
    }
    return schedule;
  }
}
