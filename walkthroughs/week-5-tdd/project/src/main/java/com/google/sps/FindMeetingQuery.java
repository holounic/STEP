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

  private interface Criterion {
    boolean satisfies(int x);
  }

  private static final int DAY = 60 * 24;

  private int getStart(Event event) {
    return event.getWhen().start();
  }

  private int getEnd(Event event) {
    return event.getWhen().end();
  }

  private boolean sameAttendees(Collection<String> a, Collection<String> b) {
    return new HashSet<>(a).removeAll(b);
  }

  private List<TimeRange> makeSchedule(int [] occupied, Criterion criterion, int duration) {
    List<TimeRange> schedule = new ArrayList<>();
    int index = 0;
    while (index < DAY) {
      int start = index;
      while (index < DAY && criterion.satisfies(occupied[index])) {
        index++;
      }
      if (index - start >= duration) {
        schedule.add(new TimeRange(start, index - start));
      }
      while (index < DAY && !criterion.satisfies(occupied[index])) {
        index++;
      }
    }
    return schedule;
  }

  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {

    Collection<String> mandatoryAttendees = request.getAttendees();
    Collection<String> optionalAttendees = request.getOptionalAttendees();
    int duration = (int) request.getDuration();
    int [] occupied = new int[DAY + 1];

    for (Event event : events) {
      if (sameAttendees(event.getAttendees(), mandatoryAttendees)) {
        for (int i = getStart(event); i < getEnd(event); i++) {
          occupied[i] = 2;
        }
      }
      if (sameAttendees(event.getAttendees(), optionalAttendees)) {
        for (int i = getStart(event); i < getEnd(event); i++) {
          occupied[i] = Math.max(1, occupied[i]);
        }
      }
    }

    List<TimeRange> commonSchedule = makeSchedule(occupied, x -> x == 0, duration);
    List<TimeRange> mandatorySchedule = makeSchedule(occupied, x -> x <= 1, duration);
    return commonSchedule.isEmpty() ? mandatorySchedule : commonSchedule;
  }
}
