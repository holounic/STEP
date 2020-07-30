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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;


public final class FindMeetingQuery {

  /*
   Criterion functional interface is used to define who's (mandatory or all attendees')
   occupied hours should be taken in account in findAvailableTimeRanges function
   */
  @FunctionalInterface
  private interface Criterion {
    boolean satisfies(int x);
  }
  private final Criterion COMMON_CRITERION = x -> x == 0;
  private final Criterion MANDATORY_CRITERION = x -> x <= 1;

  private static final int DURATION_DAY = 60 * 24;

  private int getStart(Event event) {
    return event.getWhen().start();
  }

  private int getEnd(Event event) {
    return event.getWhen().end();
  }

  private boolean haveCommonAttendees(Collection<String> a, Collection<String> b) {
    return new HashSet<>(a).removeAll(b);
  }

  /*
  The algorithm skips all minutes that don't meet the criterion,
  memorises the index of the first fitting minute in a row and goes until
  it finds the new unfitting minute. Adds the range to the list if
  if it's not less than the target duration.
   */
  /**
   * @param occupied shows what minutes are available for booking
   *                 for mandatory (0|1) or all(0) attendees
   * @param criterion defines who's (mandatory or all attendees')
   *                  occupied hours should be taken in account
   * @param duration the duration of the new meeting
   * @return the list of time ranges available to schedule the meeting
   * */
  private List<TimeRange> findAvailableTimeRanges(int [] occupied, final Criterion criterion, int duration) {
    List<TimeRange> timeRanges = new ArrayList<>();
    int index = 0;
    while (index < DURATION_DAY) {
      int start = index;
      while (index < DURATION_DAY && criterion.satisfies(occupied[index])) {
        index++;
      }
      if (index - start >= duration) {
        timeRanges.add(TimeRange.fromStartDuration(start, index - start));
      }
      while (index < DURATION_DAY && !criterion.satisfies(occupied[index])) {
        index++;
      }
    }
    return timeRanges;
  }

  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    if (events == null) {
      throw new IllegalArgumentException("Events list should be a non-null value");
    }
    if (request == null) {
      throw new IllegalArgumentException("Request should be a non-null value");
    }
    Collection<String> mandatoryAttendees = request.getAttendees();
    Collection<String> optionalAttendees = request.getOptionalAttendees();
    int duration = (int) request.getDuration();

    /*
    occupied array indicates if the i-th hour is occupied
    for mandatory (2) or optional (1) attendee
    or not occupied (0)
     */
    int [] occupied = new int[DURATION_DAY + 1];

    for (Event event : events) {
      if (haveCommonAttendees(event.getAttendees(), mandatoryAttendees)) {
        for (int i = getStart(event); i < getEnd(event); i++) {
          occupied[i] = 2;
        }
      }
      if (haveCommonAttendees(event.getAttendees(), optionalAttendees)) {
        for (int i = getStart(event); i < getEnd(event); i++) {
          occupied[i] = Math.max(1, occupied[i]);
        }
      }
    }

    List<TimeRange> commonTimeRanges = findAvailableTimeRanges(occupied, COMMON_CRITERION, duration);
    if (commonTimeRanges.isEmpty()) {
      return findAvailableTimeRanges(occupied, MANDATORY_CRITERION, duration);
    } else {
      return commonTimeRanges;
    }
  }
}
