# To-Do App Frontend (React + Vite)

Simple to-do list frontend that works with the provided backend API.

## Prerequisites
- Node.js 18+ and npm
- Backend running at `http://localhost:9090`

## Scripts
- `npm install` — install deps
- `npm run dev` — start dev server (Vite)
- `npm run build` — production build
- `npm run preview` — preview build

## Backend Endpoints Used
- GET `http://localhost:9090/tasks`
- POST `http://localhost:9090/tasks`
  - Body: `{ "title": "Finish homework", "description": "Complete math and science tasks" }`
- PUT `http://localhost:9090/tasks/{id}/complete`
- DELETE `http://localhost:9090/tasks/{id}`

## Notes
- The app assumes task shape includes at least `{ id, title, description?, completed }`.
