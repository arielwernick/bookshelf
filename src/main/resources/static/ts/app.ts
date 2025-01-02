interface Book {
    id?: number;
    title: string;
    author: string;
    pages: number;
    summary: string;
    review: string;
}

interface DragPosition {
    offset: number;
    element: Element | null;
}

class BookshelfApp {
    private books: NodeListOf<Element>;
    private form: HTMLFormElement | null;
    private modal: any;

    constructor() {
        this.books = document.querySelectorAll('.book');
        this.form = document.getElementById('addBookForm') as HTMLFormElement;
        this.initializeBooks();
        this.initializeForm();
        this.initializeDragAndDrop();
    }

    private initializeBooks(): void {
        this.books.forEach(book => {
            const bookInner = book.querySelector('.book-inner');
            const closeButton = book.querySelector('.close-button');

            bookInner?.addEventListener('click', () => {
                if (!book.classList.contains('active')) {
                    this.books.forEach(b => b.classList.remove('active'));
                    book.classList.add('active');
                }
            });

            closeButton?.addEventListener('click', (e: Event) => {
                e.stopPropagation();
                book.classList.remove('active');
            });
        });
    }

    private initializeForm(): void {
        if (!this.form) return;

        this.form.addEventListener('submit', async (e: SubmitEvent) => {
            e.preventDefault();

            if (!this.form?.checkValidity()) {
                e.stopPropagation();
                this.form?.classList.add('was-validated');
                return;
            }

            const formData: Book = {
                title: (document.getElementById('title') as HTMLInputElement)?.value.trim(),
                author: (document.getElementById('author') as HTMLInputElement)?.value.trim(),
                pages: parseInt((document.getElementById('pages') as HTMLInputElement)?.value),
                summary: (document.getElementById('summary') as HTMLTextAreaElement)?.value.trim(),
                review: (document.getElementById('review') as HTMLTextAreaElement)?.value.trim()
            };

            try {
                const response = await fetch('/api/books', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(formData)
                });

                if (response.ok) {
                    this.showSuccessMessage();
                } else {
                    const errors = await response.json();
                    this.showErrorMessage(errors);
                }
            } catch (error) {
                console.error('Error:', error);
                this.showErrorMessage(['Failed to add book']);
            }
        });

        this.initializeCharacterCounters();
    }

    private initializeCharacterCounters(): void {
        const textareas = document.querySelectorAll('textarea[maxlength]');
        textareas.forEach(textarea => {
            const counter = textarea.nextElementSibling;
            if (counter?.classList.contains('char-counter')) {
                textarea.addEventListener('input', () => {
                    counter.textContent = `${(textarea as HTMLTextAreaElement).value.length}/${textarea.getAttribute('maxlength')}`;
                });
            }
        });
    }

    private showSuccessMessage(): void {
        alert('Book added successfully!');
        window.location.reload();
    }

    private showErrorMessage(errors: string[]): void {
        alert('Error: ' + errors.join('\n'));
    }

    private initializeDragAndDrop(): void {
        const bookshelves = document.querySelectorAll('.bookshelf');
        let draggedBook: Element | null = null;

        document.querySelectorAll('.book').forEach((book: Element) => {
            const bookElement = book as HTMLElement;
            bookElement.draggable = true;

            bookElement.addEventListener('dragstart', (e: DragEvent) => {
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
        bookshelves.forEach((shelf: Element) => {
            const shelfElement = shelf as HTMLElement;

            shelfElement.addEventListener('dragover', (e: Event) => {
                e.preventDefault();
                shelfElement.classList.add('drag-over');
            });

            shelfElement.addEventListener('dragleave', () => {
                shelfElement.classList.remove('drag-over');
            });

            shelfElement.addEventListener('drop', async (e: Event) => {
                e.preventDefault();
                shelfElement.classList.remove('drag-over');
                if (!draggedBook) return;

                const booksGrid = shelfElement.querySelector('.books-grid');
                if (!booksGrid) return;

                // Move the book to the new shelf
                booksGrid.appendChild(draggedBook);

                // Get all books in their current order
                const allBooks = Array.from(document.querySelectorAll('.book'))
                    .map(book => (book as HTMLElement).getAttribute('data-book-id'))
                    .filter((id): id is string => id !== null);

                // Update the order in the database
                await this.updateBookOrder(allBooks);
            });
        });
    }

    private async updateBookOrder(bookIds: string[]): Promise<void> {
        try {
            const numericIds = bookIds.map(id => parseInt(id));
            const response = await fetch('/api/books/reorder', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(numericIds)
            });

            if (!response.ok) {
                throw new Error('Failed to update book order');
            }
        } catch (error) {
            console.error('Error updating book order:', error);
        }
    }
}

document.addEventListener('DOMContentLoaded', () => {
    new BookshelfApp();
}); 