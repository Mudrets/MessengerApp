package com.example.messengerapp.domain.usecase.people

import com.example.messengerapp.domain.entity.user.User
import com.example.messengerapp.domain.repository.PeopleRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

interface SearchPeopleUseCase : (String) -> Observable<List<User>>

class SearchPeopleUseCaseImpl @Inject constructor(
    private val repository: PeopleRepository
) : SearchPeopleUseCase {

    override fun invoke(searchQuery: String): Observable<List<User>> =
        repository.loadPeople()
            .map { people ->
                people.filter { user ->
                    user.fullName.contains(searchQuery, ignoreCase = true)
                            || user.email.contains(searchQuery, ignoreCase = true)
                }
            }

}