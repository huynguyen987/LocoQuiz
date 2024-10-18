document.addEventListener('DOMContentLoaded', function () {
    let questionCount = 0;
    let quizType = '';
    const questionsPerPage = 20;
    let currentPage = 1;
    let totalPages = 1;

    const questionCountInput = document.getElementById('questionCount');
    const quizTypeInput = document.getElementById('quizType');
    const questionGridContainer = document.getElementById('questionGridContainer');
    const questionEditor = document.getElementById('questionEditor');
    const addQuestionBtn = document.getElementById('addQuestionBtn');
    const startQuizBtn = document.getElementById('startQuizBtn');
    const quizCreatorContainer = document.getElementById('quizCreatorContainer');
    const selectedQuizTypeDiv = document.getElementById('selectedQuizType');
    const chosenQuizTypeSpan = document.getElementById('chosenQuizType');
    const quizTypeSelection = document.getElementById('quizTypeSelection');
    const quizFileInput = document.getElementById('quizFile');

    // Pagination Elements
    const prevPageBtn = document.getElementById('prevPageBtn');
    const nextPageBtn = document.getElementById('nextPageBtn');
    const currentPageSpan = document.getElementById('currentPage');
    const totalPagesSpan = document.getElementById('totalPages');

    // Dropdown Elements
    const dropdown = document.querySelector('.dropdown');
    const dropdownToggle = document.querySelector('.dropdown-toggle');
    const dropdownMenu = document.querySelector('.dropdown-menu');

    // Function to toggle dropdown
    function toggleDropdown() {
        dropdown.classList.toggle('open');
        dropdownToggle.setAttribute('aria-expanded', dropdown.classList.contains('open'));
    }

    // Close dropdown when clicking outside
    function closeDropdown(event) {
        if (!dropdown.contains(event.target)) {
            dropdown.classList.remove('open');
            dropdownToggle.setAttribute('aria-expanded', 'false');
        }
    }

    // Event listener for dropdown toggle
    dropdownToggle.addEventListener('click', function (event) {
        event.stopPropagation(); // Prevent event from bubbling up to document
        toggleDropdown();
    });

    // Event listener for clicking inside dropdown menu
    dropdownMenu.addEventListener('click', function (event) {
        event.stopPropagation(); // Prevent event from bubbling up to document
    });

    // Event listener for selecting/deselecting tags
    dropdownMenu.addEventListener('change', function () {
        updateDropdownLabel();
    });

    // Update the dropdown toggle button text based on selected tags
    function updateDropdownLabel() {
        const selectedTags = Array.from(dropdownMenu.querySelectorAll('input[type="checkbox"]:checked'))
            .map(checkbox => checkbox.parentElement.textContent.trim());
        if (selectedTags.length > 0) {
            dropdownToggle.textContent = selectedTags.join(', ');
            dropdownToggle.appendChild(createChevronIcon());
        } else {
            dropdownToggle.textContent = 'Select Tags';
            dropdownToggle.appendChild(createChevronIcon());
        }
    }

    // Function to create chevron icon
    function createChevronIcon() {
        const chevron = document.createElement('i');
        chevron.classList.add('fas', 'fa-chevron-down');
        return chevron;
    }

    // Close dropdown when clicking outside
    document.addEventListener('click', closeDropdown);

    // Start Quiz Creation
    startQuizBtn.addEventListener('click', function () {
        const selectedQuizType = document.querySelector('input[name="quizTypeRadio"]:checked');
        if (selectedQuizType) {
            quizType = selectedQuizType.value;
            quizTypeInput.value = quizType;

            // Disable quiz type selection
            quizTypeSelection.style.display = 'none';
            startQuizBtn.style.display = 'none';

            // Display selected quiz type
            chosenQuizTypeSpan.textContent = formatQuizType(quizType);
            selectedQuizTypeDiv.style.display = 'block';

            // Check if a file is uploaded
            if (quizFileInput.files.length > 0) {
                // Hide manual question creation UI
                quizCreatorContainer.style.display = 'none';
            } else {
                // Show manual question creation UI
                quizCreatorContainer.style.display = 'flex';

                // Initialize the quiz creator
                initializeQuizCreator();
            }
        } else {
            alert('Please select a quiz type before starting.');
        }
    });

    // Event listener for file input change
    quizFileInput.addEventListener('change', function () {
        if (quizFileInput.files.length > 0) {
            // Hide manual question creation UI
            quizCreatorContainer.style.display = 'none';
        } else {
            // Show manual question creation UI
            quizCreatorContainer.style.display = 'flex';
        }
    });

    function initializeQuizCreator() {
        // Add any initialization code if needed
    }

    // Function to add a question based on the quiz type
    function addQuestion() {
        questionCount++;
        questionCountInput.value = questionCount;

        // Add button to the question grid
        const questionButton = document.createElement('button');
        questionButton.type = 'button';
        questionButton.classList.add('question-btn');
        questionButton.id = `questionBtn${questionCount}`;
        questionButton.textContent = `${questionCount}`;
        questionButton.setAttribute('data-num', questionCount);
        questionGridContainer.appendChild(questionButton);

        // Add question section based on the quiz type
        let questionHTML = '';

        if (quizType === 'multiple-choice') {
            questionHTML = getMultipleChoiceQuestionHTML(questionCount);
        } else if (quizType === 'fill-in-the-blank') {
            questionHTML = getFillInTheBlankQuestionHTML(questionCount);
        } else if (quizType === 'matching') {
            questionHTML = getMatchingQuestionHTML(questionCount);
        }

        const questionSection = document.createElement('div');
        questionSection.classList.add('question-section');
        questionSection.id = `question${questionCount}`;
        questionSection.innerHTML = questionHTML;
        questionSection.setAttribute('data-answer-count', '2'); // Initialize answer count for MCQ
        questionEditor.appendChild(questionSection);

        // Update pagination
        updatePagination();

        // Show the newly added question
        showQuestion(questionCount);
        highlightActiveButton(questionCount);
    }

    // Function to show only the selected question
    function showQuestion(num) {
        const questionSections = questionEditor.querySelectorAll('.question-section');
        questionSections.forEach(section => {
            if (parseInt(section.id.replace('question', '')) === num) {
                section.classList.add('active');
            } else {
                section.classList.remove('active');
            }
        });
    }

    // Function to highlight the active question button
    function highlightActiveButton(num) {
        const questionButtons = questionGridContainer.querySelectorAll('.question-btn');
        questionButtons.forEach(btn => {
            const btnNum = parseInt(btn.getAttribute('data-num'));
            btn.classList.toggle('active', btnNum === num);
        });
    }

    // Function to remove a question
    function removeQuestion(num) {
        // Remove the question button and question section
        const questionButton = questionGridContainer.querySelector(`.question-btn[data-num="${num}"]`);
        const questionSection = questionEditor.querySelector(`#question${num}`);
        if (questionButton) questionButton.remove();
        if (questionSection) questionSection.remove();

        // Decrease questionCount and update the hidden field
        questionCount--;
        questionCountInput.value = questionCount;

        // Update IDs and numbers of subsequent questions
        updateQuestionNumbers();

        // Update pagination
        updatePagination();

        // If the current page is beyond total pages, adjust it
        if (currentPage > totalPages) {
            currentPage = totalPages;
            updatePagination();
        }
    }

    // Update question numbers after removal
    function updateQuestionNumbers() {
        const questionSections = questionEditor.querySelectorAll('.question-section');
        const questionButtons = questionGridContainer.querySelectorAll('.question-btn');
        questionSections.forEach((section, index) => {
            const newNum = index + 1;
            section.id = `question${newNum}`;
            section.querySelector('h3').textContent = `Question ${newNum} (${formatQuizType(quizType)})`;

            // Update IDs and names within the question section
            updateQuestionFields(section, newNum);
        });
        questionButtons.forEach((btn, index) => {
            const newNum = index + 1;
            btn.id = `questionBtn${newNum}`;
            btn.textContent = `${newNum}`;
            btn.setAttribute('data-num', newNum);
        });
    }

    // Helper function to update question field IDs and names
    function updateQuestionFields(section, num) {
        const inputs = section.querySelectorAll('input, textarea');
        inputs.forEach(input => {
            const name = input.name.replace(/(\d+)(_?)/g, `${num}$2`);
            const id = input.id.replace(/(\d+)(_?)/g, `${num}$2`);
            input.name = name;
            input.id = id;
            if (input.type === 'radio' || input.type === 'checkbox') {
                input.value = input.value.replace(/\d+/, num);
            }
        });
        const labels = section.querySelectorAll('label');
        labels.forEach(label => {
            const htmlFor = label.getAttribute('for');
            if (htmlFor) {
                const newFor = htmlFor.replace(/(\d+)(_?)/g, `${num}$2`);
                label.setAttribute('for', newFor);
            }
        });

        // Update data-answer-count attribute
        if (section.hasAttribute('data-answer-count')) {
            const answerOptions = section.querySelectorAll('.answer-option');
            section.setAttribute('data-answer-count', answerOptions.length);
        }

        // Update matching pairs if it's a matching question
        if (quizType === 'matching') {
            const matchingPairsContainer = section.querySelector(`#matchingPairs${num}`);
            const matchingPairs = matchingPairsContainer.querySelectorAll('.matching-pair');
            matchingPairs.forEach((pair, index) => {
                const pairNum = index + 1;
                pair.setAttribute('data-pair-num', pairNum);
                pair.querySelector('.pair-number').textContent = pairNum;

                // Update inputs and labels
                const inputA = pair.querySelector('.column-a input');
                const labelA = pair.querySelector('.column-a label');
                const inputB = pair.querySelector('.column-b input');
                const labelB = pair.querySelector('.column-b label');
                const removeBtn = pair.querySelector('.remove-pair-btn');

                inputA.id = `matchA${num}_${pairNum}`;
                inputA.name = `matchA${num}_${pairNum}`;
                inputA.placeholder = `Column A Item ${pairNum}`;
                labelA.setAttribute('for', `matchA${num}_${pairNum}`);

                inputB.id = `matchB${num}_${pairNum}`;
                inputB.name = `matchB${num}_${pairNum}`;
                inputB.placeholder = `Column B Item ${pairNum}`;
                labelB.setAttribute('for', `matchB${num}_${pairNum}`);

                removeBtn.setAttribute('data-question', num);
                removeBtn.setAttribute('data-pair', pairNum);
            });
        }
    }

    // Helper function to format quiz type
    function formatQuizType(type) {
        switch (type) {
            case 'multiple-choice':
                return 'Multiple Choice';
            case 'fill-in-the-blank':
                return 'Fill in the Blank';
            case 'matching':
                return 'Matching';
            default:
                return '';
        }
    }

    // Helper functions to get HTML for each quiz type
    function getMultipleChoiceQuestionHTML(num) {
        return `
          <h3>Question ${num} (Multiple Choice)</h3>
          <!-- Remove Question Button -->
          <button type="button" class="remove-btn" data-num="${num}">Remove Question</button>
          <!-- Question Content -->
          <label for="questionContent${num}">Question:</label>
          <textarea id="questionContent${num}" name="questionContent${num}" required aria-required="true"></textarea>

          <!-- Answers -->
          <label>Answers:</label>
          <div id="answersContainer${num}">
              ${[1, 2].map(i => `
              <div class="answer-option">
                <input type="radio" id="correctAnswer${num}_${i}" name="correctAnswer${num}" value="${i}" required aria-required="true">
                <label for="answer${num}_${i}">Answer ${i}:</label>
                <input type="text" id="answer${num}_${i}" name="answer${num}_${i}" placeholder="Answer ${i}" required aria-required="true">
              </div>
              `).join('')}
          </div>
          <button type="button" class="add-answer-btn" data-question="${num}">Add Answer</button>
        `;
    }

    function getFillInTheBlankQuestionHTML(num) {
        return `
          <h3>Question ${num} (Fill in the Blank)</h3>
          <!-- Remove Question Button -->
          <button type="button" class="remove-btn" data-num="${num}">Remove Question</button>
          <!-- Question Content -->
          <label for="questionContent${num}">Question:</label>
          <textarea id="questionContent${num}" name="questionContent${num}" required aria-required="true"></textarea>
          <!-- Correct Answer -->
          <label for="correctAnswer${num}">Correct Answer:</label>
          <input type="text" id="correctAnswer${num}" name="correctAnswer${num}" required aria-required="true">
        `;
    }

    function getMatchingQuestionHTML(num) {
        return `
          <h3>Question ${num} (Matching)</h3>
          <!-- Remove Question Button -->
          <button type="button" class="remove-btn" data-num="${num}">Remove Question</button>
          <!-- Instructions -->
          <p class="matching-instructions">Enter the items for Column A and Column B. The answers will be matched in the order you enter them.</p>
          <div class="matching-pair-header">
            <div class="pair-number">#</div>
            <div class="column-a">Column A</div>
            <div class="column-b">Column B</div>
            <div class="actions">Actions</div>
          </div>
          <div id="matchingPairs${num}" class="matching-pair-container">
            ${getMatchingPairHTML(num, 1)}
          </div>
          <button type="button" class="add-pair-btn" data-question="${num}">Add Matching Pair</button>
        `;
    }

    function getMatchingPairHTML(questionNum, pairNum) {
        return `
          <div class="matching-pair" data-pair-num="${pairNum}">
            <div class="pair-number">${pairNum}</div>
            <div class="column-a">
              <label for="matchA${questionNum}_${pairNum}">Item:</label>
              <input type="text" id="matchA${questionNum}_${pairNum}" name="matchA${questionNum}_${pairNum}" required aria-required="true" placeholder="Column A Item ${pairNum}">
            </div>
            <div class="column-b">
              <label for="matchB${questionNum}_${pairNum}">Item:</label>
              <input type="text" id="matchB${questionNum}_${pairNum}" name="matchB${questionNum}_${pairNum}" required aria-required="true" placeholder="Column B Item ${pairNum}">
            </div>
            <button type="button" class="remove-pair-btn" data-question="${questionNum}" data-pair="${pairNum}">Remove</button>
          </div>
        `;
    }

    // Function to add an answer option to a multiple-choice question
    function addAnswerOption(questionNum) {
        const questionSection = document.getElementById(`question${questionNum}`);
        let answerCount = parseInt(questionSection.getAttribute('data-answer-count'), 10);
        answerCount++;
        questionSection.setAttribute('data-answer-count', answerCount);

        const answersContainer = document.getElementById(`answersContainer${questionNum}`);
        const newAnswerHTML = `
        <div class="answer-option">
            <input type="radio" id="correctAnswer${questionNum}_${answerCount}" name="correctAnswer${questionNum}" value="${answerCount}" required aria-required="true">
            <label for="answer${questionNum}_${answerCount}">Answer ${answerCount}:</label>
            <input type="text" id="answer${questionNum}_${answerCount}" name="answer${questionNum}_${answerCount}" placeholder="Answer ${answerCount}" required aria-required="true">
        </div>
        `;
        answersContainer.insertAdjacentHTML('beforeend', newAnswerHTML);
    }

    // Function to add a matching pair to a matching question
    function addMatchingPair(questionNum) {
        const matchingPairsContainer = document.getElementById(`matchingPairs${questionNum}`);
        const currentPairs = matchingPairsContainer.querySelectorAll('.matching-pair').length;
        const newPairNum = currentPairs + 1;
        matchingPairsContainer.insertAdjacentHTML('beforeend', getMatchingPairHTML(questionNum, newPairNum));
    }

    // Event delegation for question buttons
    questionGridContainer.addEventListener('click', function (e) {
        if (e.target && e.target.classList.contains('question-btn')) {
            const num = parseInt(e.target.getAttribute('data-num'));
            showQuestion(num);
            highlightActiveButton(num);
        }
    });

    // Event delegation for question editor
    questionEditor.addEventListener('click', function (e) {
        if (e.target && e.target.classList.contains('remove-btn')) {
            const num = parseInt(e.target.getAttribute('data-num'));
            removeQuestion(num);
        }
        if (e.target && e.target.classList.contains('add-answer-btn')) {
            const questionNum = parseInt(e.target.getAttribute('data-question'));
            addAnswerOption(questionNum);
        }
        if (e.target && e.target.classList.contains('add-pair-btn')) {
            const questionNum = parseInt(e.target.getAttribute('data-question'));
            addMatchingPair(questionNum);
        }
        if (e.target && e.target.classList.contains('remove-pair-btn')) {
            const questionNum = parseInt(e.target.getAttribute('data-question'));
            const pairNum = parseInt(e.target.getAttribute('data-pair'));
            removeMatchingPair(questionNum, pairNum);
        }
    });

    // Function to remove a matching pair
    function removeMatchingPair(questionNum, pairNum) {
        const matchingPairsContainer = document.getElementById(`matchingPairs${questionNum}`);
        const pairElement = matchingPairsContainer.querySelector(`.matching-pair[data-pair-num="${pairNum}"]`);
        if (pairElement) {
            pairElement.remove();

            // Update pair numbers and attributes
            updateMatchingPairNumbers(questionNum);
        }
    }

    // Function to update matching pair numbers after removal
    function updateMatchingPairNumbers(questionNum) {
        const matchingPairsContainer = document.getElementById(`matchingPairs${questionNum}`);
        const pairs = matchingPairsContainer.querySelectorAll('.matching-pair');
        pairs.forEach((pair, index) => {
            const newPairNum = index + 1;
            pair.setAttribute('data-pair-num', newPairNum);
            pair.querySelector('.pair-number').textContent = newPairNum;

            // Update inputs and labels
            const inputA = pair.querySelector('.column-a input');
            const labelA = pair.querySelector('.column-a label');
            const inputB = pair.querySelector('.column-b input');
            const labelB = pair.querySelector('.column-b label');
            const removeBtn = pair.querySelector('.remove-pair-btn');

            inputA.id = `matchA${questionNum}_${newPairNum}`;
            inputA.name = `matchA${questionNum}_${newPairNum}`;
            inputA.placeholder = `Column A Item ${newPairNum}`;
            labelA.setAttribute('for', `matchA${questionNum}_${newPairNum}`);

            inputB.id = `matchB${questionNum}_${newPairNum}`;
            inputB.name = `matchB${questionNum}_${newPairNum}`;
            inputB.placeholder = `Column B Item ${newPairNum}`;
            labelB.setAttribute('for', `matchB${questionNum}_${newPairNum}`);

            removeBtn.setAttribute('data-pair', newPairNum);
        });
    }

    // Pagination functions
    function updatePagination() {
        const totalQuestions = questionCount;
        totalPages = Math.ceil(totalQuestions / questionsPerPage);
        currentPageSpan.textContent = currentPage;
        totalPagesSpan.textContent = totalPages;

        prevPageBtn.disabled = currentPage === 1;
        nextPageBtn.disabled = currentPage === totalPages || totalPages === 0;

        const questionButtons = questionGridContainer.querySelectorAll('.question-btn');
        questionButtons.forEach((btn, index) => {
            const btnPage = Math.ceil((index + 1) / questionsPerPage);
            btn.style.display = btnPage === currentPage ? 'block' : 'none';
        });
    }

    prevPageBtn.addEventListener('click', () => {
        if (currentPage > 1) {
            currentPage--;
            updatePagination();
        }
    });

    nextPageBtn.addEventListener('click', () => {
        if (currentPage < totalPages) {
            currentPage++;
            updatePagination();
        }
    });

    // Add question button click event
    addQuestionBtn.addEventListener('click', addQuestion);

    // Function to update dropdown label on page load
    updateDropdownLabel();
});
