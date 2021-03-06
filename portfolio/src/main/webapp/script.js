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

google.charts.load('current', {
  'packages':['geochart'],
  'mapsApiKey' : 'AIzaSyAzwDpM2F-wAKGlA5tmc_CSF_IZa2zrmJ8'
});
google.charts.setOnLoadCallback(drawChart);

const DATA_MISSING = -1;

/** Fetches the covid-19 data and uses it to create a geochart */
function drawChart() {
  fetch('/covid-19-data').then(response => response.json())
  .then((covid19Data) => {
    const data = new google.visualization.DataTable();
    data.addColumn('string', 'Country');
    data.addColumn('number', 'Active Case')
    // Add the data and the country name to the table
    Object.keys(covid19Data).forEach((country) => {
      if (covid19Data[country] != DATA_MISSING) {
        data.addRow([country, covid19Data[country]]);
      }
    });
    // Set the color of the chart
    const options = {
      colorAxis: {colors: ['white', 'orange', 'red', 'black']},
      backgroundColor: 'skyblue',
      forcelFrame: true,
    };

    const chart = new google.visualization.GeoChart(
        document.getElementById('geochartDiv'));
    chart.draw(data, options);
  });
}

/** Creates an <li> element containing text. */
function createListElement(text) {
  const liElement = document.createElement('li');
  liElement.innerText = text;
  return liElement;
}

/** Fetches comments from the datastore and adds them to DOM */
function loadComments() {
  const numComments = document.getElementById('comments-to-show').value;
  const url = `/data?comments-to-show=${numComments}`;
  fetch(url).then(response => response.json()).then((comments) => {
    // Refresh the page
    const commentsListElement = document.getElementById('comment-list');
    commentsListElement.innerText = '';
    // Display those comments as a list
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
  contentElement.innerText = comment.propertyMap.content;

  // Create the button to delete the comment
  const deleteButtonElement = document.createElement('button');
  deleteButtonElement.innerText = 'delete';
  deleteButtonElement.addEventListener('click', () => {
    // Delete the comment and remove it from the DOM
    deleteComment(comment);
    commentElement.remove();
  });

  commentElement.appendChild(contentElement);
  commentElement.appendChild(deleteButtonElement);
  return commentElement;
}

/** Tells the servlet to delete the given comment */
function deleteComment(comment) {
  const params = new URLSearchParams();
  params.append('id', comment.key.id);
  console.log(comment.id);
  fetch('/delete-data', {method: 'POST', body: params});
}

/** Fetches the URL to upload the image and show the comment form */
function fetchImageUploadUrlAndShowForm() {
  fetch('/image-upload-url').then(response => response.text())
  .then((imageUploadUrl) => {
    // Display the form elment
    const commentFormElement = document.getElementById('comment-form');
    commentFormElement.action = imageUploadUrl;
    commentFormElement.classList.remove('hidden');
  });
}
