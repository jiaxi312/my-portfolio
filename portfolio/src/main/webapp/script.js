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

/** Creates an <li> element containing text. */
function createListElement(text) {
  const liElement = document.createElement('li');
  liElement.innerText = text;
  return liElement;
}

/** Fetches comments from the datastore and adds them to DOM */
function loadComments() {
    fetch('/data').then(response => response.json()).then((comments) => {
        // Display those comments as a list
        const commentsListElement = document.getElementById('comment-list');
        comments.forEach((comment) => {
            commentsListElement.appendChild(createCommentElement(comment));
        });
    });
}

/** Creates an element that represents the given comment */
function createCommentElement(comment) {
    const commentElement = document.createElement('li');
    commentElement.className = "comment";

    // Display the content of the comment
    const contentElement = document.createElement('span');
    contentElement.innerText = comment;

    commentElement.appendChild(contentElement);
    return commentElement;
}
