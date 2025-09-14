import {inject, Injectable, Signal, signal} from '@angular/core';
import {Profile} from '../models/profile';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {catchError, Observable, throwError} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
  private httpClient = inject(HttpClient);
  private apiRequestGetUser = "http://localhost:8080/api/users/profile"
  private apiRequestDeleteUser = "http://localhost:8080/api/users/me"

  public getUser():Observable<Profile>{
    return this.httpClient.get<Profile>(this.apiRequestGetUser);
  }

  public deleteUser():Observable<string>{
    return this.httpClient.delete<string>(this.apiRequestDeleteUser)
  }
}
