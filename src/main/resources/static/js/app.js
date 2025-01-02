"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
class BookshelfApp {
    constructor() {
        this.books = document.querySelectorAll('.book');
        this.form = document.getElementById('addBookForm');
        this.initializeBooks();
        this.initializeForm();
        this.initializeDragAndDrop();
    }
    initializeBooks() {
        this.books.forEach(book => {
            const bookInner = book.querySelector('.book-inner');
            const closeButton = book.querySelector('.close-button');
            bookInner === null || bookInner === void 0 ? void 0 : bookInner.addEventListener('click', () => {
                if (!book.classList.contains('active')) {
                    this.books.forEach(b => b.classList.remove('active'));
                    book.classList.add('active');
                }
            });
            closeButton === null || closeButton === void 0 ? void 0 : closeButton.addEventListener('click', (e) => {
                e.stopPropagation();
                book.classList.remove('active');
            });
        });
    }
    initializeForm() {
        if (!this.form)
            return;
        this.form.addEventListener('submit', (e) => __awaiter(this, void 0, void 0, function* () {
            var _a, _b, _c, _d, _e, _f, _g;
            e.preventDefault();
            if (!((_a = this.form) === null || _a === void 0 ? void 0 : _a.checkValidity())) {
                e.stopPropagation();
                (_b = this.form) === null || _b === void 0 ? void 0 : _b.classList.add('was-validated');
                return;
            }
            const formData = {
                title: (_c = document.getElementById('title')) === null || _c === void 0 ? void 0 : _c.value.trim(),
                author: (_d = document.getElementById('author')) === null || _d === void 0 ? void 0 : _d.value.trim(),
                pages: parseInt((_e = document.getElementById('pages')) === null || _e === void 0 ? void 0 : _e.value),
                summary: (_f = document.getElementById('summary')) === null || _f === void 0 ? void 0 : _f.value.trim(),
                review: (_g = document.getElementById('review')) === null || _g === void 0 ? void 0 : _g.value.trim()
            };
            try {
                const response = yield fetch('/api/books', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(formData)
                });
                if (response.ok) {
                    this.showSuccessMessage();
                }
                else {
                    const errors = yield response.json();
                    this.showErrorMessage(errors);
                }
            }
            catch (error) {
                console.error('Error:', error);
                this.showErrorMessage(['Failed to add book']);
            }
        }));
        this.initializeCharacterCounters();
    }
    initializeCharacterCounters() {
        const textareas = document.querySelectorAll('textarea[maxlength]');
        textareas.forEach(textarea => {
            const counter = textarea.nextElementSibling;
            if (counter === null || counter === void 0 ? void 0 : counter.classList.contains('char-counter')) {
                textarea.addEventListener('input', () => {
                    counter.textContent = `${textarea.value.length}/${textarea.getAttribute('maxlength')}`;
                });
            }
        });
    }
    showSuccessMessage() {
        alert('Book added successfully!');
        window.location.reload();
    }
    showErrorMessage(errors) {
        alert('Error: ' + errors.join('\n'));
    }
    initializeDragAndDrop() {
        const bookshelves = document.querySelectorAll('.bookshelf');
        let draggedBook = null;
        document.querySelectorAll('.book').forEach((book) => {
            const bookElement = book;
            bookElement.draggable = true;
            bookElement.addEventListener('dragstart', (e) => {
                draggedBook = book;
                bookElement.classList.add('dragging');
                document.body.classList.add('dragging-active');
            });
            bookElement.addEventListener('dragend', () => {
                bookElement.classList.remove('dragging');
                document.body.classList.remove('dragging-active');
                draggedBook = null;
            });
        });
        // Handle dropping books on shelves
        bookshelves.forEach((shelf) => {
            const shelfElement = shelf;
            shelfElement.addEventListener('dragover', (e) => {
                e.preventDefault();
                shelfElement.classList.add('drag-over');
            });
            shelfElement.addEventListener('dragleave', () => {
                shelfElement.classList.remove('drag-over');
            });
            shelfElement.addEventListener('drop', (e) => __awaiter(this, void 0, void 0, function* () {
                e.preventDefault();
                shelfElement.classList.remove('drag-over');
                if (!draggedBook)
                    return;
                const booksGrid = shelfElement.querySelector('.books-grid');
                if (!booksGrid)
                    return;
                // Move the book to the new shelf
                booksGrid.appendChild(draggedBook);
                // Get all books in their current order
                const allBooks = Array.from(document.querySelectorAll('.book'))
                    .map(book => book.getAttribute('data-book-id'))
                    .filter((id) => id !== null);
                // Update the order in the database
                yield this.updateBookOrder(allBooks);
            }));
        });
    }
    updateBookOrder(bookIds) {
        return __awaiter(this, void 0, void 0, function* () {
            try {
                const numericIds = bookIds.map(id => parseInt(id));
                const response = yield fetch('/api/books/reorder', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(numericIds)
                });
                if (!response.ok) {
                    throw new Error('Failed to update book order');
                }
            }
            catch (error) {
                console.error('Error updating book order:', error);
            }
        });
    }
}
document.addEventListener('DOMContentLoaded', () => {
    new BookshelfApp();
});
