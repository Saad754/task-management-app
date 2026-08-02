import {inject, Service} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {CreateTaskRequest, Task, TaskPriority, TaskStatus, UpdateTaskRequest} from '../models/task';
import {environment} from '../environments/environment';

@Service()
export class TaskService {
  private http = inject(HttpClient);
  private baseUrl = `${environment.apiUrl}/tasks`;

  getTasks(status?: TaskStatus, priority?: TaskPriority): Observable<Task[]> {
    let url = this.baseUrl;
    if (status) {
      url = `${url}?status=${status}`;
    } else if (priority) {
      url = `${url}?priority=${priority}`;
    }
    return this.http.get<Task[]>(url);
  }
  getTask(id: number): Observable<Task> {
    return this.http.get<Task>(`${this.baseUrl}/${id}`);
  }
  createTask(request: CreateTaskRequest): Observable<Task> {
    return this.http.post<Task>(this.baseUrl, request);
  }
  updateTask(id: number, request: UpdateTaskRequest): Observable<Task> {
    return this.http.put<Task>(`${this.baseUrl}/${id}`, request);
  }
  deleteTask(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
